package dai.client.clientAppRoot;

import java.awt.Color;

public class SplashScreen_t {
	SplashScreen screen;

	public static void main (String[] args) {
		SplashScreen_t app = new SplashScreen_t();
		app.go();
	}

	SplashScreen_t() {
		screen = new SplashScreen(true, true);
		screen.setBackground(Color.black);
		screen.setText("Starting up...");
		screen.setVisible(true);
	}

	synchronized void go () {
		try {
		int i;

		String[] messages =
		{	"Starting database services...",
			"Preparing client interface...",
			"Doing nothing in particular...",
			"Putting on jammies...",
			"Brushing teeth...",
			"Going sleepy-pie..."
		};

		long[] delays =
		{
			3000,
			2000,
			3000,
			1500,
			1500,
			1000
		};

		long progress = 0, totalProgress = 0;
		for (i=0; i<delays.length; i++)
			totalProgress += delays[i];

		for (i=0; i<messages.length; i++) {
			screen.setText(messages[i]);
			wait(delays[i]);
			progress += delays[i];
			screen.setProgress((double)(progress)/(double)(totalProgress));
		}


		System.exit(1);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}