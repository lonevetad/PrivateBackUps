package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Test per vedere se e' possibile cambiare l'immagine di una {@link ImageIcon} (aggiunta ad una
 * JLabel o affini) senza doverne reinstanziare una nuova
 */
public class TestImageIconManuallyAnimated {
	public static final String PATH_IMAGES = "resources/img/";
	private static Color[] colorDefault = null;
	private static BufferedImage[] imageSquareColor;

	public TestImageIconManuallyAnimated() {
		canContinue = true;
	}

	boolean canContinue;
	AnimationCustom animation;
	JFrame fin;
	JLabel jl;
	ImageIcon ic;
	Thread threadRepainter;

	//

	void init() {
		System.out.println("Init Start");
		animation = createAnimation();
		fin = new JFrame("afdsds");
		fin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				canContinue = false;
			}
		});

		jl = new JLabel("animation");
		ic = new ImageIcon(animation.getImage());
		jl.setPreferredSize(new Dimension(50, 50));
		jl.setIcon(ic);
		fin.add(jl);

		threadRepainter = new Thread(() -> {
			while (canContinue) {
				ic.setImage(animation.nextImage().getImage());
				jl.repaint();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});

		fin.setSize(200, 200);
		fin.setVisible(true);
		threadRepainter.start();
	}

	public AnimationCustom createAnimation() {
		AnimationCustom ac;
		BufferedImage[] frames;
		ac = new AnimationCustom();
		frames = imagesFromFile();
		if (frames == null) frames = imagesFromColoredSquares();
		ac.setFrames(frames);
		return ac;
	}

	public static BufferedImage[] imagesFromFile() {
		int i, numFrames;
		BufferedImage[] frames;

		numFrames = 8;
		frames = new BufferedImage[numFrames];
		i = -1;
		try {
			while (++i < numFrames)
				frames[i] = ImageIO.read(new File(PATH_IMAGES + "D" + (i + 1) + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
			frames = null;
		}
		return frames;
	}

	public static BufferedImage[] imagesFromColoredSquares() {
		int w, h, rgb, i;
		Color[] cc;
		BufferedImage bi;
		if (colorDefault == null) colorDefault = new Color[] { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW,
				new Color(255, 0, 255), new Color(32, 200, 200), Color.ORANGE, Color.PINK };
		cc = colorDefault;
		if (imageSquareColor == null) {
			imageSquareColor = new BufferedImage[cc.length];
			i = -1;
			w = h = 50;
			for (Color c : cc) {
				rgb = c.getRGB();
				bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
				for (int y = 0; y < h; y++)
					for (int x = 0; x < w; x++)
						bi.setRGB(x, y, rgb);
				imageSquareColor[++i] = bi;
			}
		}
		return imageSquareColor;
	}

	//

	public static class AnimationCustom {
		public AnimationCustom() {
			currentFrame = 0;
		}

		int currentFrame;
		BufferedImage[] frames;

		public BufferedImage getImage() {
			return frames[currentFrame];
		}

		public AnimationCustom nextImage() {
			if (++currentFrame >= frames.length) currentFrame = 0;
			return this;
		}

		public BufferedImage[] getFrames() {
			return frames;
		}

		public AnimationCustom setFrames(BufferedImage[] frames) {
			this.frames = frames;
			return this;
		}

	}

	//

	public static void main(String[] args) {
		TestImageIconManuallyAnimated t;
		t = new TestImageIconManuallyAnimated();
		t.init();
	}

}