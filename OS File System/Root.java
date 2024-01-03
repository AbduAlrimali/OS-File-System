public class Root extends Folder{
    private String label;
    final private char UUID;
    protected int size;
    protected int usedSize;
    private int freeSpace;
    static int blockSize;
    private String fileType;

    Root(String name, char UUID, int size) {
        super(name, null, new StringBuilder("This PC"));
        this.UUID = UUID;
        this.size = size;
        this.fileType = "Root";
    }
    public void getFileInformation() {
        if(protection.contains("r")){
            // Implement get information operation
            System.out.println("name: " + name);
            // Output attributes
            System.out.println("UUID: " + UUID);
            System.out.println("identifier: " + identifier);
            System.out.println("type: " + fileType);
            System.out.println("full Size: " + size);
            System.out.println("used Size: " + usedSize);
            System.out.println("free Size: " + freeSpace);
            System.out.println("protection: " + protection);
            System.out.println("creation time: " + creationTime);
            System.out.println("modification time: " + modificationTime);
            System.out.println("access time: " + accessTime);
            System.out.println();
        } else {
            System.out.println("error in permissions");
        }
    }
    public void setUsedSize(){

    }
    public String toString(){
        return name + " ("+UUID+":)";
    }
    //setters and getters
    public void setLabel(String name){
        label = name;
    }
    public void setSize(int new_size){
        size = new_size;
        setFreeSize();
    }
    public static void setBlockSize(int new_size){
        blockSize = new_size;
    }
    public void setFreeSize(){
        freeSpace = size - usedSize;
    }
    //getters
    public String getLabel(){
        return label;
    }
    public char getUUID(){
        return UUID;
    }
    public int getSize(){
        return size;
    }
    public int getUsedSize(){
        return usedSize;
    }
    public int getFreeSpace(){
        return freeSpace;
    }
    public int getBlockSize(){
        return blockSize;
    }
}
