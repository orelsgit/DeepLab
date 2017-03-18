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
	
	public static int yesNo(String message, String title){
		return(JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION));
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

