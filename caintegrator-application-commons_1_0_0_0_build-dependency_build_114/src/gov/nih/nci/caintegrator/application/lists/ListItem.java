package gov.nih.nci.caintegrator.application.lists;


import java.io.Serializable;

import org.apache.log4j.Logger;

public class ListItem implements Serializable{
    
    private static Logger logger = Logger.getLogger(ListItem.class);
    private static final long serialVersionUID = 4682371205502747948L;
    private Long id;
    private String name;
    private Long rank;
    private String notes; 
    private String listName;
    private Long listId;
    
    /**
     * @return Returns the listId.
     */
    public Long getListId() {
        return listId;
    }

    /**
     * @param listId The listId to set.
     */
    public void setListId(Long listId) {
        this.listId = listId;
    }

    public ListItem(String name, String listName){
        this.name = name;
        this.listName = listName;
    }
    
    public ListItem(){}
        
    public ListItem(String name){
        this.name = name;        
    }
    
       
    
    /**
     * @return Returns the listName.
     */
    public String getListName() {
        return listName;
    }


    /**
     * @param listName The listName to set.
     */
    public void setListName(String listName) {
        this.listName = listName;
    }


     /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the notes.
     */
    public String getNotes() {
        return notes;
    }
    /**
     * @param notes The notes to set.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
    /**
     * @return Returns the rank.
     */
    public Long getRank() {
        return rank;
    }
    /**
     * @param rank The rank to set.
     */
    public void setRank(Long rank) {
        this.rank = rank;
    }


    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }


    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

}
