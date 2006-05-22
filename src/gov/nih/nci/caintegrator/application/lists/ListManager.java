/**
 * This interface will be implemented for any class that will
 * be responsible for uploading user lists into the users session
 */
package gov.nih.nci.caintegrator.application.lists;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author rossok
 * 
 */
public class ListManager {
    
private static ListManager instance = null;
    
    public ListManager(){
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static ListManager getInstance() {
        
          if (instance == null) {
            instance = new ListManager();
          }
          return instance;
    }
   
    
    public UserList createList(ListType listType, String listName, List<String> undefinedList, ListValidator validator) {
        UserList userList = new UserList();
        if(undefinedList!=null){
           List<String> validItems = validate(undefinedList, listType, validator);
           userList.setList(validItems);
            //set the name
            userList.setName(listName);
            //set the list type
            userList.setListType(listType);
            try {
                Date date = new Date();                
                userList.setDateCreated(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            userList.setItemCount(userList.getList().size());
            
            //get the invalid items
            List<String> invalidItems = getInvalid(undefinedList, listType, validator);
            userList.setInvalidList(invalidItems);
        }
        
        return userList;
    }

    @SuppressWarnings("unchecked")
    public List<String> validate(List<String> myList, ListType listType, ListValidator listValidator) {
        myList = listValidator.getValidList(listType,myList);
        return myList;
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getInvalid(List<String> myList, ListType listType, ListValidator listValidator) {
        myList = listValidator.getInvalidList(listType,myList);
        return myList;
    }

    public Map<String,String> getParams(UserList userList) {        
        Map<String, String> listParams = new HashMap<String,String>();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa", Locale.US);
        Integer count = userList.getItemCount();
        Integer icount = userList.getInvalidList().size();
        String date = dateFormat.format(userList.getDateCreated());
        listParams.put("listName", userList.getName());
        listParams.put("date", date);
        listParams.put("items", count.toString());
        listParams.put("invalidItems", icount.toString());
        listParams.put("type",userList.getListType().toString());
        
        return listParams;
    }

    
}
