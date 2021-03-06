/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.lists.ajax;

import gov.nih.nci.caintegrator.application.lists.ListManager;
import gov.nih.nci.caintegrator.application.lists.ListOrigin;
import gov.nih.nci.caintegrator.application.lists.ListSubType;
import gov.nih.nci.caintegrator.application.lists.ListType;
import gov.nih.nci.caintegrator.application.lists.ListValidator;
import gov.nih.nci.caintegrator.application.lists.UserList;
import gov.nih.nci.caintegrator.application.lists.UserListBeanHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import uk.ltd.getahead.dwr.ExecutionContext;

public class CommonListFunctions {

	
	public CommonListFunctions() {}
	
	public static String getListAsList(ListType ty){
		
		JSONObject res = new JSONObject();
		res.put("listType", ty.toString());
		
		String results = "";
		
		HttpSession session = ExecutionContext.get().getSession(false);
		UserListBeanHelper helper = new UserListBeanHelper(session);
                      
        List patientLists = helper.getLists(ty);
        if (!patientLists.isEmpty()) {
            for (int i = 0; i < patientLists.size(); i++) {
                UserList list = (UserList) patientLists.get(i);
                ListManager uploadManager = (ListManager) ListManager.getInstance();
                Map paramMap = uploadManager.getParams(list);
                String commas = StringUtils.join(list.getList().toArray(), ",");
                String sty = list.getListOrigin()!=null && !list.getListOrigin().equals(ListOrigin.Default) ? "color:#A90101" : "color:#000";
                results += ("<li id='" + paramMap.get("listName") + "' title='"+commas+"' style='"+sty+"'>"+paramMap.get("listName")+"</li>");
            }
        } else {
            results = "";
        }
        res.put("LIs", results);
		return res.toString();
	}
	
	/*
    public static String getPatientListAsList()	{
		return getListAsList(ListType.PatientDID);
	}
	
	public static String getGeneListAsList()	{
		return getListAsList(ListType.GeneSymbol);
	}
	*/
	
	//DWR ONLY: doesnt accept ListSubTypes (see below), just sets as ListSubType.Custom
	public static String createGenericList(ListType type, List<String> list, String name, ListValidator lv)	{
		//no duplicates
		HashSet<String> h = new HashSet<String>();
		for (int i = 0; i < list.size(); i++)
			h.add(list.get(i).trim());
		List<String> cleanList = new ArrayList<String>();
		for(String n : h)	{
			cleanList.add(n);
		}
		
		String success = "fail";
		ListManager um = ListManager.getInstance();
        
		try	{
			UserList mylist = um.createList(type, name, cleanList, lv);
			//set the sub-type to custom 
			mylist.setListOrigin(ListOrigin.Custom);
			//only works thru DWR
			UserListBeanHelper ulbh = new UserListBeanHelper();
			if(ulbh!=null)	{
				ulbh.addList(mylist);
				success = "pass";
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return success;
	}
	
	//DWR ONLY:  takes a list of ListSubTypes, also appends ListSubType.Custom
	public static String createGenericList(ListType type, ListSubType listSubType, List<String> list, String name, ListValidator lv)	{
		//no duplicates
		HashSet<String> h = new HashSet<String>();
		for (int i = 0; i < list.size(); i++)
			h.add(list.get(i).trim());
		List<String> cleanList = new ArrayList<String>();
		for(String n : h)	{
			cleanList.add(n);
		}
		String success = "fail";
		ListManager um = ListManager.getInstance();
		try	{
			UserList mylist = um.createList(type, name, cleanList, lv);
			if(listSubType!=null){
				mylist.setListSubType(listSubType);
                mylist.setListOrigin(ListOrigin.Custom);
			}
			else	{
				mylist.setListOrigin(ListOrigin.Custom);
			}
			//only works thru DWR
			UserListBeanHelper ulbh = new UserListBeanHelper();
			if(ulbh!=null)	{
				ulbh.addList(mylist);
				success = "pass";
			}
		}
		catch (Exception e) {}
		return success;
	}

	//CAN be used outside DWR...takes a list of ListSubTypes, also appends ListSubType.Custom
	public static String createGenericListWithSession(ListType type, ListSubType listSubType, List<String> list, String name, ListValidator lv, HttpSession session)	{
		//no duplicates
		HashSet<String> h = new HashSet<String>();
		for (int i = 0; i < list.size(); i++)
			h.add(list.get(i).trim());
		List<String> cleanList = new ArrayList<String>();
		for(String n : h)	{
			cleanList.add(n);
		}
		String success = "fail";
		ListManager um = ListManager.getInstance();
		try	{
			UserList mylist = um.createList(type, name, cleanList, lv);
			if(listSubType!=null){
				mylist.setListSubType(listSubType);
                mylist.setListOrigin(ListOrigin.Custom);
			}
			else	{
				mylist.setListOrigin(ListOrigin.Custom);
			}
			UserListBeanHelper ulbh = new UserListBeanHelper(session);
			ulbh.addList(mylist);
			success = "pass";
		}
		catch (Exception e) {}
		return success;
	}
	
	public static String exportListasTxt(String name, HttpSession session){
		String txt = "";
		UserListBeanHelper helper = new UserListBeanHelper(session);
        List<String> listItems = helper.getItemsFromList(name);
        txt = StringUtils.join(listItems.toArray(), "\r\n");
		return txt;
	}


	public static String getAllLists(List<String> listTypeList){
        
        UserListBeanHelper helper = new UserListBeanHelper();
        JSONArray listContainerArray = new JSONArray();
        for(String listType : listTypeList)	{
	        Collection<String> myLists = new ArrayList<String>();
	        
	        JSONObject listContainer = new JSONObject();
	       
	        JSONArray myJSONLists = new JSONArray();
	        
	        listContainer.put("listType", listType);
	        //which do we want to display differently in the UI
	        
	        
	        myLists = helper.getGenericListNames(ListType.valueOf(listType));
	        
	        for(String listName : myLists) {
	            UserList ul = helper.getUserList(listName);
	            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa", Locale.US);
	            if(ul!=null)  {
                    String listSubType ="";
                    if(ul.getListSubType()!=null){
                        listSubType = "Subtype: " + ul.getListSubType().toString() + " - ";
                    }
                    
                    JSONObject jsonListName = new JSONObject();
                    String listNotes = ul.getNotes();
                    if(ul.getNotes()!=null){                        
                        if(listNotes.length()>100){
                            listNotes = listNotes.substring(0,99);
                        }                        
                    }
                    
                    String style = "";
                    if(ul.getListOrigin()!=null) {
                        jsonListName.put("origin", ul.getListOrigin().toString());                        
                        if(ul.getListOrigin().equals(ListOrigin.Default)){
                            style = "color:#000000";
                            jsonListName.put("highlightType", style);
                        }
                        if(ul.getListOrigin().equals(ListOrigin.Custom)){
                            style = "color:#A90101";
                            jsonListName.put("highlightType", style);
                        }
                        
                    }
                    else{
                        jsonListName.put("highlightType", style);
                    }
                       
                    
                    
                    
                    
                    jsonListName.put("author", ul.getAuthor());
                   
                    jsonListName.put("notes", listNotes);
	                
	                jsonListName.put("listSubType", listSubType);
	                jsonListName.put("listName", ul.getName());
                    if(ul.getDateCreated()!=null){
                        jsonListName.put("listDate", dateFormat.format(ul.getDateCreated()).toString());
                    }
	                jsonListName.put("itemCount", String.valueOf(ul.getItemCount()));
	                jsonListName.put("invalidItems", String.valueOf(ul.getInvalidList().size()));
	                
	                String commas = StringUtils.join(ul.getList().toArray(), ", ");
	                jsonListName.put("listItems", commas);
	                myJSONLists.add(jsonListName);
	            }
	            
	        }
	        listContainer.put("listItems",myJSONLists);
	        listContainerArray.add(listContainer);
        }
        return listContainerArray.toString();
     }
	
	public static String uniteLists(String[] sLists, String groupName, String groupType, String action)	{
		
		JSONObject res = new JSONObject();
		String results = "pass";
		
		UserListBeanHelper helper = new UserListBeanHelper();
		try	{
			List<String> al = Arrays.asList(sLists);
			if(action.equals("join"))	{
				helper.uniteLists(al, groupName, ListType.valueOf(groupType));
			}
			else if(action.equalsIgnoreCase("difference"))	{
				helper.differenceLists(al, groupName, ListType.valueOf(groupType));
			}
			else	{
                if(helper.isIntersection(al)){
                    helper.intersectLists(al, groupName, ListType.valueOf(groupType));
                }
                else results = "fail";
			}
		}
		catch(Exception e){
			results = "fail";
		}
		res.put("results", results);
		res.put("groupType", groupType);
		res.put("action", action);
		
		return res.toString();
	}
	
	public static String[] parseListType(String combined)	{
		return StringUtils.split(combined, "|");
	}
}
