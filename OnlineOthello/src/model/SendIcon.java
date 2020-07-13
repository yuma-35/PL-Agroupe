package model;

import java.io.File;
import java.io.Serializable;

public class SendIcon implements Serializable{
	public String id ;
	public String iconName ;
	public File image;

	public SendIcon(String a, String b, File file){
		this.id = a;
		this.iconName = b;
		this.image  = file;
	}

	public File getImage() {
		return image;
	}

}
