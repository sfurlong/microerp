package dai.client.clientAppRoot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import dai.shared.cmnSvcs.SessionMetaData;

public class SplashScreen extends JWindow {

	JLabel			iconLabel = new JLabel();
	JLabel			textLabel = new JLabel();
	JProgressBar	progressBar = new JProgressBar(0, 1000);
	double			progress = 0.0;

	int padding = 5;

	public SplashScreen() {
		super();
		jbInit(false, false);
	}

	public SplashScreen(boolean textVisible, boolean progressVisible) {
		super();
		jbInit(textVisible, progressVisible);
	}

	void jbInit(boolean textVisible, boolean progressVisible) {
        SessionMetaData sessionMeta = SessionMetaData.getInstance();

        ImageIcon icon = new ImageIcon(sessionMeta.getImagesHome()+"da-ecorp.jpg");

		int x = padding, y = padding;
		int width = 0, height = 0;

		//setResizable(false);
		getContentPane().setLayout(null);

		if (icon == null) {
			iconLabel.setVisible(false);
		}
		else {
			iconLabel = new JLabel(icon);
			iconLabel.setLocation(x, y);
			iconLabel.setSize(icon.getIconWidth(), icon.getIconHeight());
			iconLabel.setVisible(true);
			getContentPane().add(iconLabel);

			width += icon.getIconWidth();
			y += icon.getIconHeight();
		}

		if (width < 300)
			width = 300;

		textLabel.setVisible(textVisible);
		if (textVisible) {
			textLabel.setLocation(x, y);
			textLabel.setSize(width, 15);
			textLabel.setVerticalAlignment(SwingConstants.CENTER);
			getContentPane().add(textLabel);

			y += 15;
		}

		progressBar.setVisible(progressVisible);
		if (progressVisible) {
			progressBar.setLocation(x, y);
			progressBar.setSize(width, 15);
			getContentPane().add(progressBar);

			y += 15;
		}

		setSize(width + 2*padding, y + padding);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		setLocation(screenSize.width/2-windowSize.width/2,
			screenSize.height/2-windowSize.height/2);
	}

	public void setBackground(Color c) {
		super.setBackground(c);
		getContentPane().setBackground(c);
	}

	public void setText(String text) {
		textLabel.setText(text);
		textLabel.repaint();
	}

	public String getText() {
		return textLabel.getText();
	}

	public void setProgress(double progress) {
		if (progress < 0.0) progress = 0.0;
		if (progress > 1.0) progress = 1.0;

		this.progress = progress;

		int min = progressBar.getMinimum();
		int value = min + (int)((double)(progressBar.getMaximum() - min) * progress);
		progressBar.setValue(value);

		if (progressBar.isVisible())
			progressBar.repaint();
	}
	
	public double getProgress() {
		return progress;
	}
	
}