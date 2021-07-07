package daiBeans;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class BrowserPanel extends JPanel
{
	// These make up the navigation bar
	JButton backButton = new JButton(new ImageIcon("left.gif"));
	JButton forwardButton = new JButton(new ImageIcon("right.gif"));
	JButton refreshButton = new JButton(new ImageIcon("refresh.gif"));
	JButton printButton = new JButton(new ImageIcon("printer.gif"));

	JLabel addrLabel = new JLabel("Address:");
	JTextField urlField = new JTextField();

	// These make up the content
	JScrollPane scrollPane = new JScrollPane();
	BrowserPane content = new BrowserPane();

	Vector URLCache = new Vector();
	int cacheIndex = 0;
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel_navPanel = new JPanel();
    XYLayout xYLayout1 = new XYLayout();

	public BrowserPanel()
	{
		super();
        try {
    		jbInit();
        } catch (Exception e) {
            System.out.println(e);
        }
	}

	private void jbInit() throws Exception
	{
		// Set up GUI stuff...
		setLayout(borderLayout1);

		// Back Button
		backButton.setLocation(new java.awt.Point(0, 0));
		backButton.setSize(new java.awt.Dimension(20, 20));
		backButton.setVisible(true);
		backButton.setToolTipText("Go Back");
		//backButton.setEnabled(false);
		jPanel_navPanel.setLayout(xYLayout1);

		backButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				backButtonActionPerformed(e);
			}
		});

		// Forward Button
		forwardButton.setLocation(new java.awt.Point(20, 0));
		forwardButton.setSize(new java.awt.Dimension(20, 20));
		forwardButton.setVisible(true);
		forwardButton.setToolTipText("Go Forward");
		//forwardButton.setEnabled(false);

		forwardButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				forwardButtonActionPerformed(e);
			}
		});

		// refresh Button
		refreshButton.setLocation(new java.awt.Point(40, 0));
		refreshButton.setSize(new java.awt.Dimension(20, 20));
		refreshButton.setVisible(true);
		refreshButton.setToolTipText("Refresh Page");
		//refreshButton.setEnabled(false);

		refreshButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				refreshButtonActionPerformed(e);
			}
		});

		// Print Button
		printButton.setLocation(new java.awt.Point(65, 0));
		printButton.setSize(new java.awt.Dimension(20, 20));
		printButton.setVisible(true);
		printButton.setToolTipText("Print");

		printButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				printButtonActionPerformed(e);
			}
		});

		// Address Label
		addrLabel.setLocation(new java.awt.Point(90, 0));
		addrLabel.setVisible(true);
		addrLabel.setSize(new java.awt.Dimension(50, 20));

		// URL Field
		urlField.setLocation(new java.awt.Point(140, 0));
		urlField.setVisible(true);
        urlField.setPreferredSize(new Dimension(150, 21));

		urlField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				urlFieldActionPerformed(e);
			}
		});

		// Content Scroll Pane
		scrollPane.setLocation(new java.awt.Point(0, 20));
		scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setVisible(true);
		this.setPreferredSize(new Dimension(600, 450));
        scrollPane.getViewport().add(content);

		add(scrollPane, BorderLayout.CENTER);
        this.add(jPanel_navPanel, BorderLayout.NORTH);
        jPanel_navPanel.add(backButton, new XYConstraints(0, 0, 26, 21));
        jPanel_navPanel.add(urlField, new XYConstraints(165, 0, 231, -1));
        jPanel_navPanel.add(forwardButton, new XYConstraints(26, 0, 26, 21));
        jPanel_navPanel.add(refreshButton, new XYConstraints(53, 0, 26, 21));
        jPanel_navPanel.add(addrLabel, new XYConstraints(114, 1, 66, 21));
        jPanel_navPanel.add(printButton, new XYConstraints(80, 0, 26, 21));
		updateButtons();

		// Register for HyperlinkEvents from content pane
		content.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				try {
					setURL(e.getURL().toString(), true);
				}
				catch(Exception ioe) {
					ioe.printStackTrace();
				}
			}
		});

	}

	public void setURL(String url, boolean caching)
	{
		try {
			//System.out.println("Setting URL to " + urlField.getText());
			urlField.setText(url);
			content.setPage(url);

			// add URL to cache
			if (caching)
				addToCache(url);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void setHTMLText(String text)
    {
        content.setContentType("text/html");
        content.setText(text);
    }

    public void hideNavBar(boolean b)
    {
        if (b) {
            jPanel_navPanel.setVisible(false);
        } else {
            jPanel_navPanel.setVisible(true);
        }
    }

	private void updateButtons()
	{
		// Back Button
		if (cacheIndex > 0) {
			//if (!backButton.isEnabled())
				backButton.setEnabled(true);
		}
		else
			//if (backButton.isEnabled());
				backButton.setEnabled(false);

		// Forward Button
		if (cacheIndex < URLCache.size()-1) {
			//if (!forwardButton.isEnabled())
				forwardButton.setEnabled(true);
		}
		else
			//if (forwardButton.isEnabled())
				forwardButton.setEnabled(false);

		// refresh Button
		if (URLCache.size() > 0)
			refreshButton.setEnabled(true);
		else
			refreshButton.setEnabled(false);

		// Print Button
		if (URLCache.size() > 0)
			printButton.setEnabled(true);
		else
			printButton.setEnabled(false);
	}

	private void addToCache(String url)
	{
		URLCache.addElement(url);
		cacheIndex = URLCache.size()-1;

		updateButtons();
	}

	private void urlFieldActionPerformed(java.awt.event.ActionEvent e)
	{
		try {
			String url = urlField.getText();
			//System.out.println("Setting URL to " + url);
			content.setURL(url);

			// add URL to cache
			addToCache(url);
		}
		catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

	private void backButtonActionPerformed(java.awt.event.ActionEvent e)
	{
		//System.out.println("Back button pressed.");

		try {
			if (cacheIndex > 0) {
				cacheIndex--;
				setURL((String)URLCache.elementAt(cacheIndex), false);
			}
			updateButtons();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void forwardButtonActionPerformed(java.awt.event.ActionEvent e)
	{
		//System.out.println("Forward button pressed.");

		try {
			if (cacheIndex < URLCache.size()-1) {
				cacheIndex++;
				setURL((String)URLCache.elementAt(cacheIndex), false);
			}
			updateButtons();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void refreshButtonActionPerformed(java.awt.event.ActionEvent e)
	{
		//System.out.println("Forward button pressed.");

		try {
			if (URLCache.size() > 0) {
				setURL((String)URLCache.elementAt(cacheIndex), false);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void printButtonActionPerformed(java.awt.event.ActionEvent e)
	{
		//System.out.println("Print button pressed.");

		try {
			if (URLCache.size() > 0) {
				// print!
				System.out.println("Spooling to the nearest printer...\n  (just kidding)");
				/*
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setJobName("BrowserPanel (" + (String)URLCache.elementAt(cacheIndex) + ")");
				if (!job.printDialog())
					return;
				*/
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void adjustSize(int width, int height)
	{
		scrollPane.setBounds(0, 20, width, height-20);
		//content.setBounds(0, 0, width, height-20);
		urlField.setBounds(140, 0, width-140, 20);
	}

    class BrowserPane extends javax.swing.JEditorPane
			implements java.util.EventListener
    {
	    String url;

	    public BrowserPane()
	    {
    		init();
	    }

	    public BrowserPane(String url)
	    {
		    init();
		    this.url = url;

		    try {
			    setURL(url);
		    }
		    catch (java.io.IOException ioe) {
			    ioe.printStackTrace();
			    url = null;
		    }
	    }

	    private void init()
	    {
		    setEditable(false);
	    }

	    public String getURL()
	    {
		    return url;
	    }

	    public void setURL(String url) throws java.io.IOException
	    {
		    try {
			    setPage(url);
		    }
		    catch (java.io.IOException ioe) {
			    url = null;
			    throw ioe;
		    }
	    }
    }
}
