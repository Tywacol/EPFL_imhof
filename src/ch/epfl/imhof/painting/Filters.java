package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/**
 * Permet de faciliter la construction des filtres
 *  
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 */
public final class Filters  {
    Predicate<Attributed<?>> filter;
    private Filters() {}
    
    /**
     * @param att attribut recherché
     * @return un prédicat n'étant vrai que si la valeur attribuée à laquelle on l'applique possède l'attribut att
     */
    public static Predicate<Attributed<?>> tagged(String att) {
      return  (a -> a.attributes(). contains(att));
      }
      
    /**
     * @param att nom de l'attribut
     * @param value nombre de valeurs d'arguments supérieur à 0
     * 
     * @return un prédicat qui n'est vrai que si la valeur attribuée à laquelle on l'applique possède un attribut portant le nom donné 
     *         et si la valeur associée à cet attribut fait partie de celles données
     */
    public static Predicate<Attributed<?>> tagged(String att, String... value) {        
        return  (a -> {
            boolean bool = false;
            for (String string : value) {
                if (string.equals(a.attributeValue(att)))
                    bool = true;
            }
       return a.attributes().contains(att) && bool ;
        });
    }
    
    /**
     * @param in nombre de couche en argument
     * 
     * @return un prédicat qui n'est vrai que lorsqu'on l'applique à une entité attribuée
     *         appartenant à la couche prise en argument.
     */
    public static Predicate<Attributed<?>> onLayer(int in) {
        return (a -> {
            int layerVal = a.attributeValue("layer", 0);
            if (in < -5 || in > 5)
                layerVal = 0;
            return layerVal == in;
        });
    }
    
    


}
