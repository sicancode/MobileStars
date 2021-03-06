package ui;
/**
 * The updates menu screen
 * @author Simon Fleming (sf58@sussex.ac.uk)
 */
import javax.microedition.lcdui.*;
import system.Application;
import resources.HTTPrequester;

public class UpdatesMenu extends Screen {
	
	// define the menu options
	private String[] menu_option = {
		"New Objects",
		"Astronmy News",
		"Weather Updates"
	};
	
	// current menu option
	private int current=0;
	
	// user message
	private String usrMsg="";
	
	// define commands
	private Command cmOK;
	private Command cmBACK;	
	
	// logo image
	private Image image;		
	
	/**
	 * Main menu constructor
	 */
	public UpdatesMenu(Application app) {
		super(app);
		
		// add commands
		cmOK    = new Command("OK", Command.ITEM, 0);
		cmBACK  = new Command("Back", Command.ITEM, 1);
		
		// add commands
		this.addCommand(cmOK);
		this.addCommand(cmBACK);
		
		// assign command listener
		this.setCommandListener(this);		
		
		// try to load image
	    try {
	        image = Image.createImage ("/logo.png"); //gal.png
	    } catch (Exception e) {
	        System.out.println("Unable to load Image: "+e);
	    }			
	}
	
	/**
	 * Update the user message
	 * @param s - string to display
	 */
	public void updateUsrMsg(String s) {
		this.usrMsg = s;
	}
	
	/**
	 * The main Canvas paint method
	 */
	public void paint( Graphics g ) {
		/** black background **/
	    g.setColor(0, 0, 0);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    
		/** page title **/
 		g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
		g.setColor(0x0FFFFFF);
		g.drawString(" - Setup Menu -", 7, 0, Graphics.TOP | Graphics.LEFT );	    

	    /** Display each menu option **/
	    for(int i=0; i<menu_option.length; i++) {
	    	if(this.app.getConfig().getLoc()==-1&&i==2) {
	    		// ignore this menu item as we have no location.
	    	} else {
		    	if(current==i) {
		    		// show highlighted colour
		    		g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
		    		g.setColor(0x0FFFFFF);
		    		g.drawString("[" + menu_option[i] + "]", 7, 7+(i+1)*20, Graphics.TOP | Graphics.LEFT );
		    	} else {
		    		// show standard colour
		    		g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		    		g.setColor(0xcccccc); 	   
		    		g.drawString(menu_option[i], 7, 7+(i+1)*20, Graphics.TOP | Graphics.LEFT );
		    	}	    	
	    	}
	    }
	    
	    /** display user message **/
	    if(this.usrMsg!=null) {
	    	g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL));
    		g.setColor(0xcccccc); 	   
    		g.drawString(usrMsg, 7, 150, Graphics.TOP | Graphics.LEFT );
	    }
	    
		// display image
		g.drawImage (image, 190, 255, Graphics.BOTTOM | Graphics.RIGHT);	   	    
	}
	
	/** Handle user key presses **/
	public void commandAction(Command c, Displayable s) {
	    if (c == cmOK) {
	       // simulate pressing key 5 / joystick down
	    	keyPressed(KEY_NUM5);
	    }
	    if(c == cmBACK) {
			// Return to the main menu
			this.app.updateScreen(0);
	    }
	}			
	
	/** Handle user key presses **/
	public void keyPressed(int code) {
		System.out.println("(" + code + ") key press!");
		// declare HTTPrequester
		HTTPrequester hr;
		Updating u;
		switch(code) {
			case KEY_NUM5: case -5: case -7:
				
				// reset user msg
				this.usrMsg = null;
				
				// action the selected option
				switch(current){
					case 0:	// GET NEW OBJECTS FROM SERVER			
						this.app.updateScreen(4);
						u = (Updating)this.app.getScreen(4);
						// create a new HTTP request Thread for updates.
					    hr = new HTTPrequester(1, app.getConfig().getLoc()+1, u, this.app);
					    new Thread(hr).start();
					break;
					case 1: // GET NEWS FROM SERVER
						this.app.updateScreen(4);
						u = (Updating)this.app.getScreen(4);						
						// create a new HTTP request Thread for updates.
					    hr = new HTTPrequester(2, app.getConfig().getLoc()+1, u, this.app);
					    new Thread(hr).start();
					break; 
					case 2: // GET WEATHER UPDATES (can only do when a location is present)
						this.app.updateScreen(4);
						u = (Updating)this.app.getScreen(4);						
						// create a new HTTP request Thread for updates.
					    hr = new HTTPrequester(4, app.getConfig().getLoc()+1, u, this.app);
					    new Thread(hr).start();
					break;
				}
					
			break;
			case KEY_NUM2: case -1:
				// move to previous item in the menu
				current = (current-1) % (menu_option.length);
				System.out.println(current);
				if(current==-1) current=menu_option.length-1; // reset current
				System.out.println(current);
				repaint();
			break;
			case KEY_NUM8: case -2:
				// move to next item in the menu
				current = (current+1) % (menu_option.length);
				System.out.println(current);
				repaint();				
			break;		
		}
		 
	}
	
}
