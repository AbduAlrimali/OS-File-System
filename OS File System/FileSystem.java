import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class FileSystem {
    public ArrayList<Root> roots = new ArrayList<>();
    public Stack<Folder> path = new Stack<>();
    public Iterator pathI;
    Scanner console = new Scanner(System.in);

    public boolean checkRootSize(Folder folder, int amount){
        if(folder.parent == null){
            Root root = (Root) folder;
            if(root.usedSize + amount > root.size) return false;
            return true;
        } else {
            Folder tmp = folder;
            while(tmp.parent != null) tmp = tmp.parent;
            Root root = (Root) tmp;
            if(root.usedSize + amount > root.size) return false;
            return true;
        }
    }
    public void calcRootSize(Folder folder, int amount){
        //amount = after - before
        //if amount is -, decrease used size
        //else increase used size

        if(amount == 0) return;
        if(amount < 0){
            if(folder.parent == null){
                ((Root)folder).usedSize -= amount;
                ((Root)folder).setFreeSize();
            } else {
                folder.usedSize -= amount;
                calcRootSize(folder.parent, amount);
            }
        } else {
            if(folder.parent == null){
                ((Root)folder).usedSize += amount;
                ((Root)folder).setFreeSize();
            } else {
                folder.usedSize += amount;
                calcRootSize(folder.parent, amount);
            }
        }
    }
    public int newRoot(String label, char UUID, int size){
        for(Root tmp:roots){
            if(tmp.getUUID() == UUID){
                System.out.println("error, the UUID already exists");
                return 0;
            }
        }
        roots.add(new Root(label, UUID, size));
        return 1;
    }
    private StringBuilder printPath(){
        pathI = path.iterator();
        StringBuilder currentPath = new StringBuilder(pathI.next()+"://");
        while (pathI.hasNext()) {
            currentPath.append(pathI.next()).append("/");
        }
        return currentPath;
    }
    public void run(){
        String fileType="";
        int input;
        Folder current = null;
        boolean run = true;
        Stack<File> copyStack = new Stack<>();
        while(run){
            if(current == null){ // outside the roots
                if(roots.size() == 0){
                    System.out.println("create new partition to start");
                    System.out.print("enter global block size: ");
                    int blockSize = console.nextInt();
                    System.out.print("enter partition name: ");
                    console.nextLine();
                    String name = console.nextLine();
                    System.out.print("enter UUID (single character): ");
                    char UUID = console.nextLine().charAt(0);
                    System.out.print("enter size: ");
                    int size = console.nextInt();
                    newRoot(name, UUID, size);
                    Root.setBlockSize(blockSize);
                } else {
                    System.out.println("To select the partition, enter 1\nTo create new partition, enter 2\nTo view partition properties, enter 3\nTo exit, enter 4");
                    input = console.nextInt();
                    switch (input) {
                        case 1 -> {
                            boolean flag = true;
                            while (flag) {
                                System.out.println("Select the partition (by UUID): ");
                                for (Root tmp : roots) {
                                    System.out.println(tmp);
                                }
                                console.nextLine();
                                input = console.nextLine().charAt(0);

                                for (Root tmp : roots) {
                                    if (tmp.getUUID() == (input)) {
                                        flag = false;
                                        path.add(tmp);
                                        current = tmp;
                                        break;
                                    }
                                }
                                if (flag) {
                                    System.out.println("Error in UUID, try again");
                                }
                            }
                        }
                        case 2 -> {
                            System.out.println("Enter the label: ");
                            console.nextLine();
                            String label = console.nextLine();
                            System.out.println("Enter UUID: ");
                            char UUID = console.nextLine().charAt(0);
                            System.out.println("Enter size: ");
                            int size = console.nextInt();
                            int value = newRoot(label, UUID, size);
                            if (value == 0) System.out.println("can't create the partition.");
                            else System.out.println("partition created successfully.");
                        }
                        case 3 -> {
                            boolean flag = true;
                            System.out.println("Select the partition (by UUID): ");
                            for (Root tmp : roots) {
                                System.out.println(tmp);
                            }
                            console.nextLine();
                            input = console.nextLine().charAt(0);

                            for (Root tmp : roots) {
                                if (tmp.getUUID() == (input)) {
                                    flag = false;
                                    tmp.getFileInformation();
                                    break;
                                }
                            }
                            if (flag) {
                                System.out.println("Error in UUID, try again");
                            }
                        }
                        case 4 -> {
                            run = false;
                            System.out.println("program terminated");
                        }
                    }

                }
            } else { // inside the roots
                System.out.println("1.\tTo list current content and path in the directory\n"+
                        "2.\tTo create a new file / folder\n" +
                        "3.\tTo read a file\n" +
                        "4.\tTo write to a file\n" +
                        "5.\tTo copy a folder / file\n" +
                        "6.\tTo delete a folder / file\n" +
                        "7.\tTo move a file / folder\n"+
                        "8.\tTo paste a file / folder\n"+
                        "9.\tTo rename a file / folder\n"+
                        "10.\tTo get file / folder information\n" +
                        "11.\tTo search in directory\n" +
                        "12.\tTo change file / folder permissions\n" +
                        "13.\tTo navigate a folder\n" +
                        "14.\tTo go to parent folder\n"+
                        "15.\tTo exit, enter 15");
                console.reset();
                input = console.nextInt();
                String name;
                boolean flag;
                switch (input) {
                    case 1 -> { //print path
                        System.out.println("current path: "+printPath());
                        current.getContents();
                    }
                    case 2 -> { //create file / directory
                        System.out.println("enter the name");
                        console.nextLine();
                        name = console.nextLine();
                        System.out.println("choose the type\n1. Folder\n2. File");
                        input = console.nextInt();
                        flag = true;
                        switch (input) {
                            case 1 -> { // folder
                                for (Folder tmp : current.getDirs()) {
                                    if (tmp.getName().equals(name)) {
                                        flag = false;
                                        System.out.println("folder already exists");
                                        break;
                                    }
                                }
                                if (flag) {
                                    if(current.getProtection().contains("w")) current.addDir(new Folder(name, current, printPath()));
                                    else System.out.println("error in permissions, can't create folder");
                                }
                            }
                            case 2 -> { // file
                                System.out.println("enter file type: ");
                                console.reset();
                                fileType = console.next();
                                for (File tmp : current.getFiles()) {
                                    if (tmp.getName().equals(name) && tmp.getFileType().equals(fileType)) {
                                        flag = false;
                                        System.out.println("file already exists");
                                        break;
                                    }
                                }
                                if (flag) {
                                    if(current.getProtection().contains("w")) current.addFile(new File(name, fileType, printPath()));
                                    else System.out.println("error in permissions, can't create file");
                                }
                            }
                            default -> System.out.println("error, try again");
                        }
                    }
                    case 3 -> { //printing content
                        System.out.println("enter the name: ");
                        console.nextLine();
                        name = console.nextLine();
                        System.out.println("enter file type: ");
                        console.reset();
                        fileType = console.next();
                        flag = true;
                        for (File tmp : current.getFiles()) {
                            if (tmp.getName().equals(name) && tmp.getFileType().equals(fileType)) {
                                flag = false;
                                System.out.println("printing file's content....");
                                tmp.getContent();
                                break;
                            }
                        }
                        if (flag) System.out.println("error, try again");
                    }


                    case 4 -> { //write to a file
                        System.out.println("enter the name");
                        console.nextLine();
                        name = console.nextLine();
                        System.out.println("enter file type: ");
                        console.reset();
                        fileType = console.next();
                        flag = true;
                        for (File tmp : current.getFiles()) {
                            if (tmp.getName().equals(name) && tmp.getFileType().equals(fileType)) {
                                flag = false;
                                System.out.println("enter the new content: ");
                                console.nextLine();
                                String content = console.nextLine();
                                int diff = content.length() - tmp.getSize();
                                System.out.println("setting the file's content....");
                                if(checkRootSize(current, diff)){
                                    if(current.getProtection().contains("w") && tmp.setContent(content) == 1) calcRootSize(current, diff);
                                }
                                else System.out.println("error, partition reached maximum size, can't write to the file");
                                break;
                            }
                        }
                        if (flag) System.out.println("error, try again");
                    }
                    case 5 -> { //copy
                        System.out.println("enter the name");
                        console.nextLine();
                        name = console.nextLine();
                        System.out.println("choose the type\n1. Folder\n2. File");
                        input = console.nextInt();
                        flag = true;
                        switch (input) {
                            case 1 -> { // folder
                                for (Folder tmp : current.getDirs()) {
                                    if (tmp.getName().equals(name)) {
                                        flag = false;
                                        copyStack.push(tmp);
                                        System.out.println("folder copied successfully");
                                        break;
                                    }
                                }
                                if (flag) System.out.println("error, try again");
                            }
                            case 2 -> { // file
                                System.out.println("enter file type: ");
                                console.reset();
                                fileType = console.next();
                                for (File tmp : current.getFiles()) {
                                    if (tmp.getName().equals(name) && tmp.getFileType().equals(fileType)) {
                                        flag = false;
                                        copyStack.push(tmp);
                                        System.out.println("file copied successfully");
                                        break;
                                    }
                                }
                                if (flag) System.out.println("error, try again");
                            }
                            default -> System.out.println("error, try again");
                        }
                    }
                    case 6 -> { //delete
                        System.out.println("enter the name");
                        console.nextLine();
                        name = console.nextLine();
                        System.out.println("choose the type\n1. Folder\n2. File");
                        input = console.nextInt();
                        flag = true;
                        switch (input) {
                            case 1 -> { // folder
                                for (Folder tmp : current.getDirs()) {
                                    if (tmp.getName().equals(name)) {
                                        flag = false;
                                        if(current.getProtection().contains("w")) current.removeDir(tmp);
                                        else System.out.println("error in permissions, can't delete folder");
                                        break;
                                    }
                                }
                                if (flag) System.out.println("error, try again");
                            }
                            case 2 -> { // file
                                System.out.println("enter file type: ");
                                console.reset();
                                fileType = console.next();
                                for (File tmp : current.getFiles()) {
                                    if (tmp.getName().equals(name) && tmp.getFileType().equals(fileType)) {
                                        flag = false;
                                        if(current.getProtection().contains("w")) current.removeFile(tmp);
                                        else System.out.println("error in permissions, can't delete file");
                                        break;
                                    }
                                }
                                if (flag) System.out.println("error, try again");
                            }
                            default -> System.out.println("error, try again");
                        }
                    }
                    case 7 -> { //move
                        System.out.println("enter the name");
                        console.nextLine();
                        name = console.nextLine();
                        System.out.println("choose the type\n1. Folder\n2. File");
                        input = console.nextInt();
                        flag = true;
                        switch (input) {
                            case 1 -> { // folder
                                for (Folder tmp : current.getDirs()) {
                                    if (tmp.getName().equals(name)) {
                                        flag = false;
                                        if(current.getProtection().contains("w")){
                                            copyStack.push(tmp);
                                            current.removeDir(tmp);
                                        } else System.out.println("error in permissions, can't move folder");
                                        break;
                                    }
                                }
                                if (flag) System.out.println("error, try again");
                            }
                            case 2 -> { // file
                                System.out.println("enter file type: ");
                                console.reset();
                                fileType = console.next();
                                for (File tmp : current.getFiles()) {
                                    if (tmp.getName().equals(name) && tmp.getFileType().equals(fileType)) {
                                        flag = false;
                                        if(current.getProtection().contains("w")){
                                            copyStack.push(tmp);
                                            current.removeFile(tmp);
                                        } else System.out.println("error in permissions, can't move file");
                                        break;
                                    }
                                }
                                if (flag) System.out.println("error, try again");
                            }
                            default -> System.out.println("error, try again");
                        }
                    }
                    case 8 -> { //paste
                        flag = false;
                        if(copyStack.isEmpty()){
                            System.out.println("the stack is empty");
                        } else if (copyStack.peek().fileType.equals("Dir")){
                            File currentFile = copyStack.peek();
                            for (File tmp : current.getDirs()) {
                                if (tmp.getName().equals(currentFile.getName())) {
                                    flag = true;
                                    break;
                                }
                            }
                            if(flag){
                                System.out.println("folder already exists");
                            } else {
                                if(current.getProtection().contains("w")){
                                    Folder tmp = (Folder) copyStack.peek();
                                    tmp.setLocation(printPath());
                                    current.addDir(tmp);
                                } else {
                                    System.out.println("error in permissions, can't paste");
                                }
                            }
                        } else {
                            File currentFile = copyStack.peek();
                            for (File tmp : current.getFiles()) {
                                if (tmp.getName().equals(currentFile.getName()) && tmp.getFileType().equals(currentFile.getFileType())) {
                                    flag = true;
                                    break;
                                }
                            }
                            if(flag){
                                System.out.println("file already exists");
                            } else {
                                if(current.getProtection().contains("w")){
                                    File tmp = copyStack.peek();
                                    tmp.setLocation(printPath());
                                    current.addFile(tmp);
                                } else {
                                    System.out.println("error in permissions, can't paste");
                                }
                            }
                        }
                    }
                    case 9 -> { //rename
                        System.out.println("enter the old name");
                        console.nextLine();
                        String name_old = console.nextLine();
                        System.out.println("Enter the new name");
                        console.nextLine();
                        name = console.nextLine();
                        System.out.println("choose the type\n1. Folder\n2. File");
                        input = console.nextInt();
                        flag = true;
                        switch (input) {
                            case 1 -> { // folder
                                for (Folder tmp : current.getDirs()) {
                                    if (tmp.getName().equals(name)) {
                                        flag = false;
                                        System.out.println("folder already exists");
                                        break;
                                    }
                                }
                                if (flag){
                                    if(current.getProtection().contains("w")) {
                                        for (Folder tmp : current.getDirs()) {
                                            if (tmp.getName().equals(name_old)) {
                                                tmp.setName(name);
                                            }
                                        }
                                    } else System.out.println("error in permissions, can't rename the folder");
                                }
                            }
                            case 2 -> { // file
                                System.out.println("enter old file type: ");
                                console.reset();
                                String fileType_old = console.next();
                                System.out.println("enter new file type (enter same type for no change): ");
                                console.reset();
                                fileType = console.next();
                                for (File tmp : current.getFiles()) {
                                    if (tmp.getName().equals(name) && tmp.getFileType().equals(fileType)) {
                                        flag = false;
                                        System.out.println("file already exists");
                                        break;
                                    }
                                }
                                if (flag){
                                    if(current.getProtection().contains("w")) {
                                        for (File tmp : current.getFiles()) {
                                            if (tmp.getName().equals(name_old) && tmp.getFileType().equals(fileType_old)) {
                                                tmp.setName(name);
                                                tmp.setFileType(fileType);
                                            }
                                        }
                                    } else System.out.println("error in permissions, can't rename the file");
                                }
                            }
                            default -> System.out.println("error, try again");
                        }
                    }
                    case 10 -> { //list info
                        System.out.println("enter the name");
                        console.nextLine();
                        name = console.nextLine();
                        System.out.println("choose the type\n1. Folder\n2. File");
                        input = console.nextInt();
                        flag = true;
                        switch (input) {
                            case 1 -> { // folder
                                for (Folder tmp : current.getDirs()) {
                                    if (tmp.getName().equals(name)) {
                                        flag = false;
                                        tmp.getFileInformation();
                                        break;
                                    }
                                }
                                if (flag) System.out.println("error, try again");
                            }
                            case 2 -> { // file
                                System.out.println("enter file type: ");
                                fileType = console.next();
                                for (File tmp : current.getFiles()) {
                                    if (tmp.getName().equals(name) && tmp.getFileType().equals(fileType)) {
                                        flag = false;
                                        tmp.getFileInformation();
                                        break;
                                    }
                                }
                                if (flag) System.out.println("error, try again");
                            }
                            default -> System.out.println("error, try again");
                        }
                    }

                    case 11 -> { //search
                        System.out.println("enter the name");
                        console.nextLine();
                        name = console.nextLine();
                        current.search(name);
                    }
                    case 12 -> { // new permissions
                        System.out.println("enter the name");
                        console.nextLine();
                        name = console.nextLine();
                        System.out.println("choose the type\n1. Folder\n2. File");
                        input = console.nextInt();
                        if(input == 2){
                            System.out.println("enter file type: ");
                            fileType = console.next();
                        }
                        System.out.println("enter the new permissions: ");
                        console.nextLine();
                        String permissions = console.nextLine();
                        flag = true;
                        switch (input) {
                            case 1 -> { // folder
                                for (Folder tmp : current.getDirs()) {
                                    if (tmp.getName().equals(name)) {
                                        flag = false;
                                        tmp.setPermissions(permissions);
                                        break;
                                    }
                                }
                                if (flag) System.out.println("error, try again");
                            }
                            case 2 -> { // file
                                for (File tmp : current.getFiles()) {
                                    if (tmp.getName().equals(name) && tmp.getFileType().equals(fileType)) {
                                        flag = false;
                                        tmp.setPermissions(permissions);
                                        break;
                                    }
                                }
                                if (flag) System.out.println("error, try again");
                            }
                            default -> System.out.println("error, try again");
                        }
                    }
                    case 13 -> { //navigate to folder
                        System.out.println("enter the name");
                        console.nextLine();
                        name = console.nextLine();
                        flag = true;
                        for (Folder tmp : current.getDirs()) {
                            if (tmp.getName().equals(name)) {
                                flag = false;
                                if(tmp.getProtection().contains("r")){
                                    current = tmp;
                                    path.add(tmp);
                                } else {
                                    System.out.println("error in permissions, can't open");
                                }
                                break;
                            }
                        }
                        if (flag) System.out.println("error, try again");
                    }
                    case 14 ->{ // move to parent
                        current = current.parent;
                        path.pop();
                    }

                    case 15 -> { //terminate
                        run = false;
                        System.out.println("program terminated");
                    }
                    default -> System.out.println("error, try again");
                }

            }
        }
        System.out.println("exit from file system successfully");
    }
}