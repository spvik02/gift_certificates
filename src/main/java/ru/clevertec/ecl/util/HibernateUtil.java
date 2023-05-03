package ru.clevertec.ecl.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Tag;

@UtilityClass
public class HibernateUtil {
    public SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Tag.class);
        configuration.addAnnotatedClass(GiftCertificate.class);
        configuration.configure();
        return configuration.buildSessionFactory();
    }
}
