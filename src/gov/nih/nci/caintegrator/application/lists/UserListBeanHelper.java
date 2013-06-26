/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

/**
 * This helper accesses the UserListBean and can be instantiated
 * anywhere. Depending on how and where the developer instantiates this class
 * the Helper can will access the UserListBean through the session, by
 * either recieving the session itself, or, in this case, having DWR access
 * the UserListBean on it own.
 */
package gov.nih.nci.caintegrator.application.lists;


import gov.nih.nci.caintegrator.application.cache.CacheConstants;
import gov.nih.nci.caintegrator.application.cache.PresentationCacheManager;
import gov.nih.nci.caintegrator.application.cache.PresentationTierCache;
import gov.nih.nci.caintegrator.dto.de.GeneIdentifierDE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import uk.ltd.getahead.dwr.ExecutionContext;

/**
 * @author rossok
 * UserListBeanHelper is the touchpoint for any app using list management service
 * The class uses the presentation tier cache to store a userlist bean containing
 * all user lists.
 *
 */
public class UserListBeanHelper{
    private HttpSession session;
    private String sessionId;
    private UserListBean userListBean;
    private static Logger logger = Logger.getLogger(UserListBeanHelper.class);
    public PresentationTierCache presentationTierCache = PresentationCacheManager.getInstance();
    
    /**
     * recommended constructor
     * @param presentationCacheId
     */
    public UserListBeanHelper(String presentationCacheId){
        this.userListBean = (UserListBean)presentationTierCache.getNonPersistableObjectFromSessionCache(presentationCacheId,CacheConstants.USER_LISTS);
    }
    
    public UserListBeanHelper(String presentationCacheId, String cacheKey){
        this.userListBean = (UserListBean)presentationTierCache.getNonPersistableObjectFromSessionCache(presentationCacheId, cacheKey);
    }
    
    public void addBean(String presntationCacheId, String key, UserListBean userListBean){
        presentationTierCache.addNonPersistableToSessionCache(presntationCacheId, CacheConstants.USER_LISTS, userListBean);        
    }
    
    /**
     * deprecated constructor -- used to support legacy code
     * @param userListBean
     * @deprecated
     */
    public UserListBeanHelper(UserListBean userListBean){
        this.userListBean = userListBean;
    }
    
    /**
     * deprecated constructor -- used to support legacy code
     * @param userListBean
     * @deprecated
     */    
    public UserListBeanHelper(HttpSession session){
        this.userListBean = (UserListBean)presentationTierCache.getNonPersistableObjectFromSessionCache(session.getId(),CacheConstants.USER_LISTS);                 
    }
    
    /**
     * deprecated constructor -- used to support legacy code
     * @param userListBean
     * @deprecated
     */
    public UserListBeanHelper(){       
        session = ExecutionContext.get().getSession(false); 
        sessionId = ExecutionContext.get().getSession(false).getId(); 
        this.userListBean = (UserListBean)presentationTierCache.getNonPersistableObjectFromSessionCache(session.getId(),CacheConstants.USER_LISTS);                 
    }
    
    
    public void addList(UserList userList) {
    	if(userListBean != null)
    		userListBean.addList(userList);        
    }
    
    public void removeList(String listName) {
    	if(userListBean != null)
    		userListBean.removeList(listName); 
    }
    
    public boolean listExists(String listName){ 
    	if(userListBean != null)
    		return userListBean.listExists(listName);
    	else return false;
    }    
    
    public String removeListFromAjax(String listName)   {
        removeList(listName);
        return listName;
    }
    
    public void addItemToList(String listName, String listItem) {
    	if(userListBean != null){
	        UserList userList =  userListBean.getList(listName);
	        ListItem item = new ListItem(listItem,listName);
	        userList.getListItems().add(item);
	        userList.setItemCount(userList.getItemCount()+1); 
    	}
    }
    
    public void removeItemFromList(String listName, String listItem) {        
        String[] listItemArray;
        if(listItem.contains(" notes")){
            listItemArray = listItem.split(" notes");
            listItem = listItemArray[0];
        }
        else if(listItem.contains(" rank")){
            listItemArray = listItem.split(" rank");
            listItem = listItemArray[0];
        }
        if(userListBean != null){
	        UserList userList =  userListBean.getList(listName);
	        for(ListItem l:userList.getListItems()){
	            if(l.getName().equalsIgnoreCase(listItem)){
	                userList.getListItems().remove(l);
	                break;
	            }
	        }
	        //userList.getList().remove(listItem);
	        userList.setItemCount(userList.getItemCount()-1);   
        }
    }
    
    public UserList getUserList(String listName){
    	if(userListBean != null)
    		return userListBean.getList(listName);      
    	else
    		return null;
    }
    
    
    public ListType getUserListType(String listName){
    	if(userListBean != null){
        UserList myList = userListBean.getList(listName); 
        return myList.getListType();
    	}else {
    		return null;
    	}
    }
    
    public String getDetailsFromList(String listName) {
    	JSONObject listDetails = new JSONObject();
    	if(userListBean != null){
        UserList userList = userListBean.getList(listName);
        
        
        listDetails.put("listName",userList.getName());
        listDetails.put("listType",userList.getListType().toString());     
        
        /**
         * new way of retrieving details from lists. Each
         * listItem is treated as such, and NOT just a string.*/
         
//        if(userList.getListItems()!=null && !userList.getListItems().isEmpty()){
//            JSONArray listItems = new JSONArray();             
//            
//            String itemDescription = "";
//            for(ListItem listItem:userList.getListItems()){
//                String name = listItem.getName();
//                itemDescription = name;
//                if(listItem.getNotes()!=null && listItem.getNotes().trim().length()>=1){
//                    itemDescription += " notes: " + listItem.getNotes();
//                }
//                if(listItem.getRank()!=null){
//                    itemDescription += " rank: " + Double.toString(listItem.getRank());
//                }
//                
//                listItems.add(itemDescription);
//            }
//            listDetails.put("validItems", listItems );
//        }
        
        if(userList.getListItems()!=null && !userList.getListItems().isEmpty()){
            JSONArray listItems = new JSONArray();   
            
            for(ListItem listItem:userList.getListItems()){
                String name = listItem.getName();
                JSONObject itemDescription = new JSONObject();
                itemDescription.put("name",name);
                if(listItem.getNotes()!=null && listItem.getNotes().trim().length()>=1){
                    itemDescription.put("notes",listItem.getNotes());
                }
                if(listItem.getRank()!=null){
                    itemDescription.put("rank",Long.toString(listItem.getRank()));
                }
                
                listItems.add(itemDescription);
            }
            listDetails.put("validItems", listItems );
        }
        
        
       /** if(userList.getList()!=null && !userList.getList().isEmpty()){
            JSONArray item = new JSONArray();             
            for(String i : userList.getList()){
                //Element item = list.addElement("item");
                //item.addText(i);
                item.add(i);
            }
            listDetails.put("validItems", item );
        }
        */
        
        if(userList.getInvalidList()!=null && !userList.getInvalidList().isEmpty()){
            JSONArray item = new JSONArray();
            for(String v : userList.getInvalidList()){
                //Element item = list.addElement("invalidItem");
                //item.addText(v);
                item.add(v);
            }
            listDetails.put("invalidItems", item );
        }
    	}
        return listDetails.toString();
    }
    
    public List<UserList> getLists(ListType listType) {
        List<UserList> typeList = new ArrayList<UserList>();
        
        if(userListBean!=null && userListBean.getEntireList()!=null && !userListBean.getEntireList().isEmpty()){
            for(UserList list : userListBean.getEntireList()){
                if(list.getListType() == listType){
                    typeList.add(list);
                }
            }
        }
        return typeList;
    }
    
    
    public List<UserList> getLists(ListType listType, ListSubType listSubType)   {
        //get lists of this type with these subtypes
        List<UserList> ul = this.getLists(listType);
        List<UserList> matches = new ArrayList<UserList>(); 
        //got all the main type, now see if any of these are of any listsubtypes
        for(UserList u : ul){
            ListSubType lst = u.getListSubType();            
                if(lst.equals(listSubType)){
                    matches.add(u);
                }            
        }
        return matches;
    }
    
    public List<UserList> getLists(ListType listType, List<ListSubType> listSubTypes)   {
        //get lists of this type with these subtypes
        List<UserList> ul = this.getLists(listType);
        List<UserList> matches = new ArrayList<UserList>(); 
        //got all the main type, now see if any of these are of any listsubtypes
        for(UserList u : ul){
            for(ListSubType subType:listSubTypes){
                ListSubType lst = u.getListSubType();            
                    if(lst.equals(subType)){
                        matches.add(u);
                    }    
            }        
        }
        return matches;
    }
    
    public List<UserList> getLists(ListType listType, ListOrigin listOrigin)   {
        //get lists of this type with these subtypes
        List<UserList> ul = this.getLists(listType);
        List<UserList> matches = new ArrayList<UserList>(); 
        //got all the main type, now see if any of these are of any listsubtypes
        for(UserList u : ul){
            ListOrigin lo = u.getListOrigin();
                if(lo.equals(listOrigin)){
                    matches.add(u);
                }            
        }
        return matches;
    }
    
    
    
    public List<UserList> getAllCustomLists(){
        List<UserList> customList = new ArrayList<UserList>();
        if (userListBean == null || userListBean.getEntireList().isEmpty())
            return customList;
        
        for (UserList list : userListBean.getEntireList()){
            ListOrigin origin = list.getListOrigin();
            if(origin!=null && origin.equals(ListOrigin.Custom)){
                customList.add(list);
            }
        }
        return customList;
    }
    
    public List<UserList> getAllCustomListsForType(ListType type){
        List<UserList> customList = new ArrayList<UserList>();
        if (userListBean == null || userListBean.getEntireList().isEmpty())
            return customList;
        
        for (UserList list : userListBean.getEntireList()){
            ListOrigin origin = list.getListOrigin();
            ListType myType = list.getListType();
            if(origin!=null && origin.equals(ListOrigin.Custom)
                    && myType!=null && myType == type){
                customList.add(list);
            }
        }
        return customList;
    }
    
    
    
    public List<UserList> getAllLists() {
        List<UserList> allList = new ArrayList<UserList>();
        if(userListBean!=null){
            for(UserList list : userListBean.getEntireList()){
                allList.add(list);
            } 
        }
        return allList;
    }
    
    public List<UserList> getAllDeletedCustomLists(){
        List<UserList> removedLists = new ArrayList<UserList>();
        if (userListBean == null || userListBean.getRemovedLists().isEmpty())
            return removedLists;
        
        for (UserList list : userListBean.getRemovedLists()){
            ListOrigin origin = list.getListOrigin();
            if(origin!=null && origin.equals(ListOrigin.Custom)){
                removedLists.add(list);
            }
        }
        return removedLists;
    }
    public void clearAllDeletedCustomLists(){
    	 userListBean.clearRemovedLists();
    }
    public List<String> getItemsFromList(String listName){
        UserList userList = userListBean.getList(listName);
        List<String> items = userList.getList();        
        return items;
    }
   
     
    
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
    
    public Collection getGenericListNamesFromStringWithSubs(String listType, String sub){ 
        //parse the subs
        ListSubType lst = ListSubType.valueOf(sub.trim());
        return getGenericListNamesWithSubTypes(ListType.valueOf(listType), lst);
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
    
    public Collection getGenericListNamesWithSubTypes(ListType lt, ListSubType sub)  {
         Collection<UserList> setList = new ArrayList<UserList>();
         setList = getLists(lt, sub);
         Collection setListNames = new ArrayList();
         for(UserList userListName : setList){
             setListNames.add(userListName.toString());
         }
         return setListNames;
    }
    
    public void differenceLists(List<String> listNames, String newListName, ListType listType)  {
        
        //we're only expecting 2 lists here
        if(listNames.size()!=2) {
            return;
        }
        UserList ulist = userListBean.getList(listNames.get(0));
        List<String> s1 = ulist.getList();
        ulist = userListBean.getList(listNames.get(1));
        List<String> s2 = ulist.getList();
        

        //transforms s1 into the (asymmetric) set difference of s1 and s2. 
        //(For example, the set difference of s1 minus s2 is the set containing all 
        //the elements found in s1 but not in s2.)

        Set<String> difference = new HashSet<String>(s1);
        difference.removeAll(s2);
        UserList newList = null;
        if(difference.size()>0){
            List dList = new ArrayList();
            dList.addAll(difference);
            Collections.sort(dList, String.CASE_INSENSITIVE_ORDER);
            
            newList = new UserList(newListName+"_"+listNames.get(0)+"-"+listNames.get(1),listType,dList,new ArrayList<String>(),new Date());
            newList.setListOrigin(ListOrigin.Custom);
            newList.setItemCount(dList.size());
            userListBean.addList(newList);
        }
        Set<String> difference2 = new HashSet<String>(s2);
        difference2.removeAll(s1);
        if(difference2.size()>0)    {
            List dList2 = new ArrayList();
            dList2.addAll(difference2);
            Collections.sort(dList2, String.CASE_INSENSITIVE_ORDER);
            newList = null;
            newList = new UserList(newListName+"_"+listNames.get(1)+"-"+listNames.get(0),listType,dList2,new ArrayList<String>(),new Date());
            newList.setListOrigin(ListOrigin.Custom);
            newList.setItemCount(dList2.size());
            userListBean.addList(newList);
        }
        
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
        Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
        UserList newList = new UserList(newListName,listType,items,new ArrayList<String>(),new Date());
        newList.setListOrigin(ListOrigin.Custom);
        newList.setItemCount(items.size());
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
        Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
        UserList newList = new UserList(newListName,listType,items,new ArrayList<String>(),new Date());
        newList.setListOrigin(ListOrigin.Custom);
        newList.setItemCount(items.size());
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

	public UserListBean getUserListBean() {
		return userListBean;
	}


}
