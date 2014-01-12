package com.nokia.ticToe;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;


public class TicToe extends MIDlet {

	private Form choose_level;
	private Form splash;
	
	private boolean[][] done = new boolean [10][200];



	public TicToe() {
		// TODO Auto-generated constructor stub
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	private String readLine(InputStreamReader reader) throws IOException {
		int readChar = reader.read();
		if (readChar == -1) {
			return null;
		}
		StringBuffer string = new StringBuffer("");
		while (readChar != -1 && readChar != '\n') {
			if (readChar != '\r') {
				string.append((char)readChar);
			}
			readChar = reader.read();
		}
		return string.toString();
	}
	
	public boolean writeFile(byte[] data) 
    {
        javax.microedition.io.Connection c = null;
        java.io.OutputStream os = null;
        try {
            c = javax.microedition.io.Connector.open("/done.txt", javax.microedition.io.Connector.READ_WRITE);
            javax.microedition.io.file.FileConnection fc = 
                    (javax.microedition.io.file.FileConnection) c;
            if (!fc.exists())
                fc.create();
            else
                fc.truncate(0);
            os = fc.openOutputStream();
            os.write(data);
            os.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null)
                    os.close();
                if (c != null)
                    c.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
	
	
	private void writeSaveData(int level)
	{
		Form ss = new Form("ss");
		
		String[] values = new String[10];
		InputStreamReader reader = new InputStreamReader(
				getClass().getResourceAsStream("/done.txt"));
		int j = 0;
		int k = 0;
		String line = null;
		try {
			while ((line = readLine(reader)) != null){
				values[j] = line;
				if(j == level - 1){
					k = j;
				}
				j++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int val = Integer.parseInt(values[k]);
		val++;
		values[k] = Integer.toString(val);
		
		
		String data = "";
		
		for(int i=0;i<9;i++)
		{
			data = data + values[i];
		}
		
		
		Label label=new Label();
		label.setText(data);
		ss.addComponent(label);
		ss.show();
		
		
	}
	
	private void displayQuestion(final int level,final String[] images,final String[] answers,final int num_ques,final Form start_level)
	{
		Random rand = new Random();
		int new_ques = rand.nextInt(num_ques);
		
		int got = 1;
		
		if(done[level][new_ques] == true)
		{
			int i;
			got = 0;
			for(i=new_ques - 1;i>=0;i--)
			{
				if(done[level][i] == false)
				{
					new_ques = i;
					got = 1;
					break;
				}
			}
			
			if(got == 0)
			{
				for(i=new_ques+1;i<num_ques;i++)
				{
					if(done[level][i] == false)
					{
						new_ques = i;
						got = 1;
						break;
					}
				}
			}
			
		}
		
		if(got == 0)
		{
			Dialog dialog = new Dialog("Information");
			dialog.setScrollable(false);
			dialog.setTimeout(3000);
			Dialog.setAutoAdjustDialogSize(true);
			TextArea textArea = new TextArea("Level Completed!!");
			textArea.setEditable(false);
			textArea.setFocusable(false);
			dialog.addComponent(textArea);
			dialog.show(0, 10, 2, 10, true);
			return;
		}
		
		final int ques_num = new_ques;
		
		String imageLoc = "/logos/level_"+Integer.toString(level)+"/"+images[ques_num];
		try {
			Image img;
			img = Image.createImage(imageLoc);
			Label ques_image = new Label(img);
			ques_image.setAlignment(Component.CENTER);
			start_level.addComponent(ques_image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final TextArea answer = new TextArea("");
		start_level.addComponent(answer);
		
		final Button answerButton  = new Button("Check");
		start_level.addComponent(answerButton);
		
		answerButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				String textEntered = answer.getText();
				
						
				if(textEntered.equals(answers[ques_num]))
				{
					Dialog dialog = new Dialog("Information");
					dialog.setScrollable(false);
					dialog.setTimeout(3000);
					Dialog.setAutoAdjustDialogSize(true);
					TextArea textArea = new TextArea("Correct Answer!!");
					textArea.setEditable(false);
					textArea.setFocusable(false);
					dialog.addComponent(textArea);
					dialog.show(0, 10, 2, 10, true);
					
					done[level][ques_num] = true;
					
					start_level.removeAll();
					displayQuestion(level, images, answers, num_ques,start_level);
					
					start_level.show();
					
					//splash.show();
					
					
				}
				else
				{
					Dialog dialog = new Dialog("Information");
					dialog.setScrollable(false);
					dialog.setTimeout(3000);
					Dialog.setAutoAdjustDialogSize(true);
					TextArea textArea = new TextArea("Wrong Answer!!");
					TextArea textArea2 = new TextArea("Try Again..");
					textArea.setEditable(false);
					textArea.setFocusable(false);
					dialog.addComponent(textArea);
					textArea2.setEditable(false);
					textArea2.setFocusable(false);
					dialog.addComponent(textArea2);
					dialog.show(0, 10, 2, 10, true);
				}
			}
		});
		
	}

	protected void openLevel(Button button , final int i){

		final Form start_level = new Form("Questions!!");
		BoxLayout boxLayout = new BoxLayout(BoxLayout.Y_AXIS);
		start_level.setLayout(boxLayout);
		
		
		//start_level.removeAll();
		final String[] images = new String[200];
		final String[] answers = new String[200];

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//Read Files
				String images_file = "/logos/level_" +Integer.toString(i)+"/level_"+Integer.toString(i)+".txt";
				InputStreamReader reader = new InputStreamReader(
						getClass().getResourceAsStream(images_file));
				int j = 0;
				String line = null;
				try {
					while ((line = readLine(reader)) != null){
						images[j++] = line;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				String answer_file = "/logos/level_" +Integer.toString(i)+"/levelAnswers_"+Integer.toString(i)+".txt";
				
				reader = new InputStreamReader(
						getClass().getResourceAsStream(answer_file));
				int m = 0;
				try {
					while ((line = readLine(reader)) != null){
						answers[m++] = line;
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			
				
				
				displayQuestion(i, images, answers, j,start_level);
				
				start_level.show();
			}
		});

		Command backCmd = new Command("Back"){
			public void actionPerformed(ActionEvent evt) {
				splash.show();
			}
		};
		start_level.addCommand(backCmd);
		start_level.setBackCommand(backCmd);

	}
	
	protected void initialize()
	{
		int i,j;
		for(i=0;i<10;i++)
			for(j=0;j<200;j++)
				done[i][j] = false;
	}

	protected void startApp() throws MIDletStateChangeException {
		Display.init(this);
		
		initialize();
		
		splash = new Form("Splash");
		splash.setLayout(new BorderLayout());

		try {
			Image img;
			img = Image.createImage("/logos2.png");
			Label splash_screen = new Label(img);
			splash.addComponent(BorderLayout.CENTER,splash_screen);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final Button button  = new Button("Start");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				choose_level = new Form("Choose Level");
				BoxLayout boxLayout = new BoxLayout(BoxLayout.Y_AXIS);
				choose_level.setLayout(boxLayout);

				Label label=new Label();
				label.setText("Select Level!!");
				label.setAlignment(Component.CENTER);
				choose_level.addComponent(label);

				final Button[] level_button;
				level_button = new Button[9];

				for(int i=0;i<9;i++)
				{
					level_button[i] = new Button("Level " + Integer.toString(i+1));
					choose_level.addComponent(level_button[i]);

					openLevel(level_button[i],i+1);

				}
				choose_level.setScrollableY(true);
				choose_level.show();


				Command backCmd = new Command("Back"){
					public void actionPerformed(ActionEvent evt) {
						splash.show();
					}
				};
				choose_level.addCommand(backCmd);
				choose_level.setBackCommand(backCmd);
			}

		});

		splash.addComponent(BorderLayout.SOUTH,button);
		splash.show();
	}

}
