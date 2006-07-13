/**
 * This helper accesses the UserListBean and can be instantiated
 * anywhere. Depending on how and where the developer instantiates this class
 * the Helper can will access the UserListBean through the session, by
 * either recieving the session itself, or, in this case, having DWR access
 * the UserListBean on it own.
 */
package gov.nih.nci.caintegrator.application.lists;


import gov.nih.nci.caintegrator.application.cache.CacheConstants;
import gov.nih.nci.caintegrator.dto.de.GeneIdentifierDE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.ListUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import uk.ltd.getahead.dwr.ExecutionContext;

/**
 * @author rossok
 *
 */
public class UserListBeanHelper{
    private HttpSession session;
    private String sessionId;
    private UserListBean userListBean;
    private static Logger logger = Logger.getLogger(UserListBeanHelper.class);
     
    public UserListBeanHelper(UserListBean userListBean){
        this.userListBean = userListBean;
    }
    
    public UserListBeanHelper(HttpSession session){
        userListBean = (UserListBean) session.getAttribute(CacheConstants.USER_LISTS);        
        
    }
    public UserListBeanHelper(){       
        session = ExecutionContext.get().getSession(false); 
        sessionId = ExecutionContext.get().getSession(false).getId(); 
        userListBean = (UserListBean) session.getAttribute(CacheConstants.USER_LISTS);        
               
    }
    public void addList(UserList userList) {
        userListBean.addList(userList);        
    }
    
    public void removeList(String listName) {
        userListBean.removeList(listName); 
    }
    
    public String removeListFromAjax(String listName)	{
    	removeList(listName);
    	return listName;
    }
    
    public void addItemToList(String listName, String listItem) {
        UserList userList =  userListBean.getList(listName);
        userList.getList().add(listItem);
    }
    
    public void removeItemFromList(String listName, String listItem) {        
        UserList userList =  userListBean.getList(listName);
        userList.getList().remove(listItem);
    }
    
    public UserList getUserList(String listName){
    	return userListBean.getList(listName);    	
    }
    
    
    public ListType getUserListType(String listName){
        UserList myList = userListBean.getList(listName); 
        return myList.getListType();
    }
    
    public String getDetailsFromList(String listName) {
        UserList userList = userListBean.getList(listName);
        
        JSONObject listDetails = new JSONObject();
        listDetails.put("listName",userList.getName());
        listDetails.put("listType",userList.getListType().toString());
        
        /*
        Document document =  DocumentHelper.createDocument();
        Element list = document.addElement("list");
        Element type = list.addAttribute("type", userList.getListType().toString());
        Element name = list.addAttribute("name", userList.getName());
        */
        if(userList.getList()!=null && !userList.getList().isEmpty()){
        	JSONArray item = new JSONArray();
            for(String i : userList.getList()){
            	//Element item = list.addElement("item");
            	//item.addText(i);
            	item.add(i);
            }
            listDetails.put("validItems", item );
        }
        
        
        if(userList.getInvalidList()!=null && !userList.getInvalidList().isEmpty()){
        	JSONArray item = new JSONArray();
            for(String v : userList.getInvalidList()){
                //Element item = list.addElement("invalidItem");
                //item.addText(v);
            	item.add(v);
            }
            listDetails.put("invalidItems", item );
        }
        
        return listDetails.toString();
    }
    
    public List<UserList> getLists(ListType listType) {
        List<UserList> typeList = new ArrayList<UserList>();
        
        if(!userListBean.getEntireList().isEmpty()){
            for(UserList list : userListBean.getEntireList()){
                if(list.getListType() == listType){
                    typeList.add(list);
                }
            }
        }
        return typeList;
    }
    
    //needs some work
    public List<UserList> getLists(ListType listType, List<ListSubType> listSubTypes)	{
    	List<UserList> ul = this.getLists(listType);
    	List<UserList> matches = new ArrayList<UserList>();
    	for(UserList u : ul){
    		List hits = ListUtils.intersection(listSubTypes, u.getListSubType());
    		if(hits.isEmpty() || hits.size() < 1){
    			//ul.remove(u);
    			matches.add(u);
    		}
    		/*
    		for(ListSubType lst : u.getListSubType())	{
    			if(listSubTypes.contains(lst))	{
    				ul.remove(u);
    				break;
    			}
    		}
    		*/
    	}
    	return ul;
    }
    
    public List<UserList> getLists(ListType listType, ListSubType listSubType){
    	List<ListSubType> lst = new ArrayList();
    	lst.add(listSubType);
    	return getLists(listType, lst);
    }
    
    public List<UserList> getListsBySubType(List<ListSubType> listSubTypes){
    	return new ArrayList();
    }
    
    public List<UserList> getAllLists() {
        List<UserList> allList = new ArrayList<UserList>();
    
        for(UserList list : userListBean.getEntireList()){
            allList.add(list);
        }        
        return allList;
    }
    
    public List<String> getItemsFromList(String listName){
        UserList userList = userListBean.getList(listName);
        List<String> items = userList.getList();        
        return items;
    }
   
    /*
    public List<String> getGeneSymbolListNames(){ 
        Collection<UserList> geneSetList = new ArrayList<UserList>();
        geneSetList = getLists(ListType.GeneSymbol);  
        List<String> geneSetListNames = new ArrayList<String>();
        for(UserList userListName : geneSetList){
            geneSetListNames.add(userListName.toString());
        }
        return geneSetListNames;
    }
    */
    
    /**
     * @NOTE : DE may change in future.
     */
            
    public Collection<GeneIdentifierDE> getGeneDEforList(String listName){
        UserList geneSetList = userListBean.getList(listName);
        Collection<GeneIdentifierDE> geneIdentifierDECollection = new ArrayList<GeneIdentifierDE>();
        for(String item : geneSetList.getList()){
            GeneIdentifierDE.GeneSymbol gs = new GeneIdentifierDE.GeneSymbol(item);
            geneIdentifierDECollection.add(gs);
        }
        return geneIdentifierDECollection;
    }
    
    /*
    public Collection getDefaultPatientListNames(){ 
        Collection<UserList> patientSetList = new ArrayList<UserList>();
        patientSetList = getLists(ListType.PatientDID, ListSubType.Default);  
        Collection patientSetListNames = new ArrayList();
        for(UserList userListName : patientSetList){
            patientSetListNames.add(userListName.toString());
        }
        return patientSetListNames;
    }
    */
    
    public Collection getPatientListNames(){ 
        Collection<UserList> patientSetList = new ArrayList<UserList>();
        patientSetList = getLists(ListType.PatientDID);  
        Collection patientSetListNames = new ArrayList();
        for(UserList userListName : patientSetList){
            patientSetListNames.add(userListName.toString());
        }
        return patientSetListNames;
    }
    
    public Collection getGenericListNamesFromString(String listType){ 
    	return getGenericListNames(ListType.valueOf(listType));
    }
    
    public Collection getGenericListNames(ListType listType){ 
        Collection<UserList> setList = new ArrayList<UserList>();
        setList = getLists(listType);  
        Collection setListNames = new ArrayList();
        for(UserList userListName : setList){
            setListNames.add(userListName.toString());
        }
        return setListNames;
    }
     

    public void uniteLists(List<String> listNames, String newListName, ListType listType) {
        List<String> items = new ArrayList<String>();
        for(String listName: listNames){
            UserList list = userListBean.getList(listName);
            if(!list.getList().isEmpty()){
                items.addAll(list.getList());
            }
        }
        Set<String> unitedSet = new HashSet<String>(items);
        items.clear();
        items.addAll(unitedSet);
        UserList newList = new UserList(newListName,listType,items,new ArrayList<String>(),new Date());
        newList.setListSubType(ListSubType.Custom);
        userListBean.addList(newList);
    }

    public void intersectLists(List<String> listNames, String newListName, ListType listType) {
        List<String> items = new ArrayList<String>();
        List<UserList> lists = new ArrayList<UserList>();
        for(String listName: listNames){
            UserList list = userListBean.getList(listName);
            lists.add(list);
            if(!list.getList().isEmpty()){
                items.addAll(list.getList());
            }
        }
        Set<String> intersectedList = new HashSet<String>(items);
        for(UserList ul : lists){
            intersectedList.retainAll(ul.getList());
        }
        items.clear();
        items.addAll(intersectedList);
        UserList newList = new UserList(newListName,listType,items,new ArrayList<String>(),new Date());
        newList.setListSubType(ListSubType.Custom);
        userListBean.addList(newList);
    }
    
    public boolean isIntersection(List<String> listNames){
        boolean intersection = false;
        List<String> items = new ArrayList<String>();
        List<UserList> lists = new ArrayList<UserList>();
        for(String listName: listNames){
            UserList list = userListBean.getList(listName);
            lists.add(list);
            if(!list.getList().isEmpty()){
                List<String> itemsInList = list.getList();
                for(String i : itemsInList){
                    if(items.contains(i)){
                        intersection = true;
                        break;
                    }
                    else{
                      items.add(i);
                    }
                }
            }
        }
       return intersection;
    }
    

}
