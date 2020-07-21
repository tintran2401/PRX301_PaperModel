package tintt.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import tintt.entities.Model;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-07-21T22:49:09")
@StaticMetamodel(Tag.class)
public class Tag_ { 

    public static volatile SingularAttribute<Tag, String> name;
    public static volatile CollectionAttribute<Tag, Model> modelCollection;
    public static volatile SingularAttribute<Tag, String> id;

}