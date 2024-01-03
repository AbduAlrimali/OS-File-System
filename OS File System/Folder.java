import java.time.LocalDateTime;
import java.util.ArrayList;

public class Folder extends File {
    protected Folder parent;
    protected ArrayList<File> files = new ArrayList<>();
    protected ArrayList<Folder> nextDirs = new ArrayList<>();

    Folder(String name, Folder parent, StringBuilder location){
        super(name, "Dir", location);
        this.parent = parent;
    }
    public void getFileInformation() {
        if(protection.contains("r")){
            // Implement get file information operation
            System.out.println("name: " + name);
            // Output file attributes
            System.out.println("identifier: " + identifier);
            System.out.println("location: " + location);
            System.out.println("type: " + fileType);
            System.out.println("usedSize: " + usedSize);
            System.out.println("protection: " + protection);
            System.out.println("creation time: " + creationTime);
            System.out.println("modification time: " + modificationTime);
            System.out.println("access time: " + accessTime);
            System.out.println();
        } else {
            System.out.println("error in permissions");
        }
    }
    public ArrayList<File> getFiles(){
        return files;
    }
    public ArrayList<Folder> getDirs(){
        return nextDirs;
    }
    public void addFile(File tmp){
        Folder trav = this;
        while(trav.parent != null) trav = trav.parent;
        if(((Root) trav).usedSize + tmp.getSize() > ((Root) trav).getSize()) System.out.println("can't add the file, root reached maximum size");
        else {
            files.add(tmp);
            System.out.println("file created successfully");
            if(this.parent  == null) {
                ((Root) this).usedSize += tmp.getSize();
                ((Root) this).setFreeSize();
            }
            else usedSize += tmp.getSize();
            Folder tmp2 = this.parent;
            if(tmp2 != null){
                while(tmp2.parent != null){
                    tmp2.usedSize += tmp.getSize();
                    tmp2 = tmp2.parent;
                }
                ((Root) tmp2).usedSize += tmp.getSize();
                ((Root) tmp2).setFreeSize();
            }
        }
    }
    public void addDir(Folder tmp){
        Folder trav = this;
        while(trav.parent != null) trav = trav.parent;
        if(((Root) trav).usedSize + tmp.getSize() > ((Root) trav).getSize()) System.out.println("can't add the folder, root reached maximum size");
        else {
            nextDirs.add(tmp);
            System.out.println("folder created successfully");
            if(this.parent  == null) {
                ((Root) this).usedSize += tmp.getSize();
                ((Root) this).setFreeSize();
            }
            else usedSize += tmp.getSize();
            Folder tmp2 = this.parent;
            if(tmp2 != null){
                while(tmp2.parent != null){
                    tmp2.usedSize += tmp.getSize();
                    tmp2 = tmp2.parent;
                }
                ((Root) tmp2).usedSize += tmp.getSize();
                ((Root) tmp2).setFreeSize();
            }
        }
    }
    public void removeFile(File tmp){
        files.remove(tmp);
        System.out.println("file removed successfully");
        if(this.parent  == null) {
            ((Root) this).usedSize -= tmp.getSize();
            ((Root) this).setFreeSize();
        }
        else usedSize -= tmp.getSize();
        Folder tmp2 = this.parent;
        if(tmp2 != null){
            while(tmp2.parent != null){
                tmp2.usedSize -= tmp.getSize();
                tmp2 = tmp2.parent;
            }
            ((Root) tmp2).usedSize -= tmp.getSize();
            ((Root) tmp2).setFreeSize();
        }
    }
    public void removeDir(Folder tmp){
        nextDirs.remove(tmp);
        System.out.println("folder removed successfully");
        if(this.parent  == null) {
            ((Root) this).usedSize -= tmp.getSize();
            ((Root) this).setFreeSize();
        }
        else usedSize -= tmp.getSize();
        Folder tmp2 = this.parent;
        if(tmp2 != null){
            while(tmp2.parent != null){
                tmp2.usedSize -= tmp.getSize();
                tmp2 = tmp2.parent;
            }
            ((Root) tmp2).usedSize -= tmp.getSize();
            ((Root) tmp2).setFreeSize();
        }
    }
    public String toString(){
        return name;
    }
    public void search(String line){
        System.out.println("Searching folders: ");
        for(Folder tmp: nextDirs){
            if(tmp.getName().contains(line)) System.out.println(tmp);
        }
        System.out.println();
        System.out.println("Searching files: ");
        for(File tmp: files){
            if(tmp.getName().contains(line)) System.out.println(tmp);
        }
        System.out.println();
    }
    public void getContents(){
        System.out.println("Folders: ");
        for(Folder tmp: nextDirs){
            System.out.println(tmp);
        }
        System.out.println();
        System.out.println("Files: ");
        for(File tmp: files){
            System.out.println(tmp);
        }
        System.out.println();
        LocalDateTime myDateObj = LocalDateTime.now();
        this.accessTime = myDateObj.format(myFormatObj);
    }
    public void calcSize(){
        int sum = 0;
        for(File tmp: files){
            sum += tmp.getSize();
        }
        for(Folder tmp: nextDirs){
            sum += tmp.usedSize;
        }
        this.usedSize = sum;
    }
}
