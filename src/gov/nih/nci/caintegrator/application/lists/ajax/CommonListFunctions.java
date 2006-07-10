package gov.nih.nci.caintegrator.application.lists.ajax;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import gov.nih.nci.caintegrator.application.lists.ListManager;
import gov.nih.nci.caintegrator.application.lists.ListSubType;
import gov.nih.nci.caintegrator.application.lists.ListType;
import gov.nih.nci.caintegrator.application.lists.ListValidator;
import gov.nih.nci.caintegrator.application.lists.UserList;
import gov.nih.nci.caintegrator.application.lists.UserListBean;
import gov.nih.nci.caintegrator.application.lists.UserListBeanHelper;

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
                String sty = list.getListSubType()!=null && !list.getListSubType().contains(ListSubType.Default) ? "color:#A90101" : "";
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
	
	public static String createGenericList(ListType type, String[] list, String name, ListValidator lv)	{
		//no duplicates
		HashSet<String> h = new HashSet<String>();
		for (int i = 0; i < list.length; i++)
			h.add(list[i].trim());
		List<String> cleanList = new ArrayList<String>();
		for(String n : h)	{
			cleanList.add(n);
		}
		
		String success = "fail";
		ListManager um = ListManager.getInstance();
		try	{
			UserList mylist = um.createList(type, name, cleanList, lv);
			//set the sub-type to custom 
			mylist.setListSubType(ListSubType.Custom);
			UserListBeanHelper ulbh = new UserListBeanHelper();
			ulbh.addList(mylist);
			success = "pass";
		}
		catch (Exception e) {
			// TODO: handle exception
		}
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
	        listContainer.put("highlightType", ListSubType.Default.toString());
	        
	        myLists = helper.getGenericListNames(ListType.valueOf(listType));
	        
	        for(String listName : myLists) {
	            UserList ul = helper.getUserList(listName);
	            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa", Locale.US);
	            if(ul!=null)  {
	            	JSONArray listSubTypes = new JSONArray();
	            	for(ListSubType lst : ul.getListSubType()){
	            		listSubTypes.add(lst.toString());
	            	}
	                JSONObject jsonListName = new JSONObject();
	                jsonListName.put("listSubTypes", listSubTypes);
	                jsonListName.put("listName", ul.getName());
	                jsonListName.put("listDate", dateFormat.format(ul.getDateCreated()).toString());
	                jsonListName.put("itemCount", String.valueOf(ul.getItemCount()));
	                jsonListName.put("invalidItems", String.valueOf(ul.getInvalidList().size()));
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
}
