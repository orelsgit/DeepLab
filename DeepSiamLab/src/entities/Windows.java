package entities;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * This class will provide the user with easy to use JOPtionPane windows methods.
 *
 */
public class Windows {

	public static void warning(String str){
		JOptionPane.showMessageDialog(null, str, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	public static String input(String message, String title){
		return(JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE));
	}
	/**
	 * JOptionPane.YES_OPTION(0) - An integer that implies the response was positive. 
	 * JOptionPane.NO_OPTION(1) - An integer that implies the response was negative.
	 * @param message
	 * @param title
	 * @return
	 */
	public static boolean yesNoEdited(String message, String title, String yes, String no){
		String[] options = new String[2];
		options[0] = new String(yes);
		options[1] = new String(no);
		if(JOptionPane.showOptionDialog(null,message,title, 0,JOptionPane.INFORMATION_MESSAGE,null,options,null)==0)
			return true;
		return false;
	}
	

	public static boolean yesNo(String message, String title){
		if(JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION)==0)
			return true;
		return false;
	}

	public static void threadWarning(String str){
		Thread thread = new Thread(){
			public void run(){
				createCloseTimer(3).start();
				JOptionPane.showMessageDialog(null, str, "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		};thread.start();
	}


	public static void threadMessage(String str, String title){
		Thread thread = new Thread(){
			public void run(){
				createCloseTimer(3).start();
				JOptionPane.showMessageDialog(null, str, title, JOptionPane.INFORMATION_MESSAGE);
			}
		};thread.start();
	}


	public static void message(String message, String title){
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}


	private static Timer createCloseTimer(int seconds) {
		ActionListener close = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Window[] windows = Window.getWindows();
				for (Window window : windows) {
					if (window instanceof JDialog) {
						JDialog dialog = (JDialog) window;
						if (dialog.getContentPane().getComponentCount() == 1
								&& dialog.getContentPane().getComponent(0) instanceof JOptionPane){
							dialog.dispose();
						}
					}
				}

			}

		};
		Timer t = new Timer(seconds * 1000, close);
		t.setRepeats(false);
		return t;
	}
}

