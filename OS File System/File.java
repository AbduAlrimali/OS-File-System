import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class

class File {
    protected static int id = 1;
    protected String name;
    final protected int identifier;
    protected StringBuilder location;
    protected String fileType;
    protected int usedSize;
    protected int blocks;
    protected String protection;
    final protected String creationTime;
    protected String modificationTime;
    protected String accessTime;
    protected String content;
    final protected DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public File(String name, String fileType, StringBuilder location) {
        this.name = name;
        this.identifier = id++;
        this.location = location;
        this.fileType = fileType;
        this.protection = "rw";
        LocalDateTime myDateObj = LocalDateTime.now();
        this.creationTime = myDateObj.format(myFormatObj);
        this.modificationTime = this.creationTime;
        this.accessTime = this.creationTime;
        content = "";
        this.usedSize = 0;
    }
    public String toString(){
        return name+"."+fileType;
    }

    // Getters
    public void getFileInformation() {
        if(protection.contains("r")){
            // Implement get file information operation
            System.out.println("name: " + name);
            // Output file attributes
            System.out.println("identifier: " + identifier);
            System.out.println("location: " + location);
            System.out.println("type: " + fileType);
            System.out.println("usedSize: " + usedSize);
            System.out.println("blocks: " + blocks);
            System.out.println("protection: " + protection);
            System.out.println("creation time: " + creationTime);
            System.out.println("modification time: " + modificationTime);
            System.out.println("access time: " + accessTime);
            System.out.println();
        } else {
            System.out.println("error in permissions");
        }
    }
    public StringBuilder getLocation(){
        return location;
    }
    public String getName(){
        return name;
    }
    public int getSize(){
        return usedSize;
    }
    public void getContent() {
        if(protection.contains("r")){
            LocalDateTime myDateObj = LocalDateTime.now();
            this.accessTime = myDateObj.format(myFormatObj);
            System.out.println(content);
        }
        else {
            System.out.println("error in permissions");
        }
    }
    public String getCreationTime(){
        return creationTime;
    }
    public String getModificationTime(){
        return modificationTime;
    }
    public String getAccessTime(){
        return accessTime;
    }
    public int getID(){
        return identifier;
    }
    public int getBlocks(){
        return blocks;
    }
    public String getFileType(){
        return fileType;
    }
    public String getProtection(){
        return protection;
    }
    //Setters
    public void setName(String name){
        this.name = name;
    }

    public int setContent(String newContent) {
        // Implement write content operation
        if(!protection.contains("x") && protection.contains("w")){
            this.content = newContent;
            LocalDateTime myDateObj = LocalDateTime.now();
            this.modificationTime = myDateObj.format(myFormatObj);
            this.accessTime = myDateObj.format(myFormatObj);
            usedSize = content.length();
            blocks =  (int) Math.ceil((double)usedSize/Root.blockSize);
            return 1;
        }else {
            System.out.println("error in permissions");
            return 0;
        }
    }
    public void setFileType(String fileType){
        this.fileType = fileType;
    }

    public void setPermissions(String newPermissions) {
        // Implement change permissions operation
        this.protection = newPermissions;
    }
    public void setLocation(StringBuilder new_loc){
        location = new_loc;
    }
}