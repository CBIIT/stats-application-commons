package gov.nih.nci.caintegrator.application.lists;

import java.io.Serializable;

public class ListItem implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 4682371205502747948L;
    private Long id;
    private String name;
    private Double rank;
    private String notes; 
    private String listName;
    
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
    public Double getRank() {
        return rank;
    }
    /**
     * @param rank The rank to set.
     */
    public void setRank(Double rank) {
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
