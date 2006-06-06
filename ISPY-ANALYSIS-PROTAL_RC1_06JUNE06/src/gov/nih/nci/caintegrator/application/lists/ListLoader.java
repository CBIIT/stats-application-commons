package gov.nih.nci.caintegrator.application.lists;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

public class ListLoader {
    private static Logger logger = Logger.getLogger(ListLoader.class);
    
    /**
     * this method assumes for now, that there is a directory called data_files in the project where
     * preloaded lists are to be stored and referenced from some "master" file upon successful login.
     * The method parses this "master" text file that describes each of the files to be preloaded. It decides what type
     * the list needs to be and creates appropriate userlist objects accordingly. If this is
     * successful, the lists are place in the UserListBean...currently held in the session.
     * 
     * @param userListBean
     * @param fileName
     * @param listValidator
     * @return
     * @author KR
     */
   
    public static UserListBean loadLists(UserListBean userListBean, String fileName, String dataFilePath, ListValidator listValidator) {
        String masterFilePath = dataFilePath + File.separatorChar + fileName;
        logger.info("Initializing "+ fileName);
        File masterUserFile = new File(masterFilePath);        
        
        /*initialize the types of lists we want. The format for each is the name of the list,
         * as a text file, followed by # sign, then the type of lists.
         * In this case, an example would be, testList.txt#GeneSymbol
         */
        List<String> allLists = UserListGenerator.generateList(masterUserFile);
        ListManager listManager = ListManager.getInstance();
        /*
         * go through the master file, read in the names and types of lists to be created,
         * then, one by one, parse through the list and create appropriate UserLists and place in the
         * UserListBean. 
         */
        if(!allLists.isEmpty()){
            for(String f : allLists){
                String[] userList = f.split("#");
                String textFileName = userList[0];
                String listType = userList[1];
                int count = 0;
                
                //instantiate the enum listType
                ListType myType = ListType.valueOf(listType);
                
                //find the appropriate text file and create a list of strings
                try{
                    textFileName = dataFilePath + File.separatorChar + textFileName;
                    logger.info("Initializing "+textFileName);
                    File userTextFile = new File(textFileName);
                    List<String> myTextList = UserListGenerator.generateList(userTextFile);
                    
                    //now I have a listype and the list of string I want, create a userList from this                    
                    UserList myUserList = listManager.createList(myType, "default"+myType.toString()+(count+1), myTextList, listValidator);
                    
                    //place the list in the userListBean
                    count++;
                    if(myUserList!=null){
                        userListBean.addList(myUserList);
                    }
                }catch(NullPointerException e){
                    logger.error("file was not found. " + e);               
                }
            }
        }
        //return populated userlistBean
        return userListBean;
    }

}
