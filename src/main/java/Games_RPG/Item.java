/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
public abstract class Item {
    protected String itemName;
    protected String rarity;
    
    public Item(String itemName, String rarity){
        this.itemName = itemName;
        this.rarity = rarity;
    }
    
    public abstract String use(Entity user);
    
    public String getItemName(){
        return this.itemName;
    }
    public String getRarity(){
        return this.rarity;
    }
}
