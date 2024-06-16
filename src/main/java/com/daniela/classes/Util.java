package com.daniela.classes;

import java.util.ArrayList;

public class Util {
    /**
     * Convertir String a Integer, permite nulos y cadenas vacías
     *
     * @param s String con un numero
     * @return Valor numérico del String o null si el String es nulo o está vacío
     */
    public static Integer parseInt(String s) {
        Integer num = null;

        if (s != null && !s.isBlank()) num = Integer.parseInt(s);

        return num;
    }

    /**
     * Convertir datos obtenidos del formulario en un array de tipo Email[]
     *
     * @param emails          Array de emails
     * @param emailCategories Array de categorías de los emails
     * @return Lista de tipo Email
     */
    public static ArrayList<Email> emailsFromArrays(String[] emails, String[] emailCategories) {
        ArrayList<Email> emailList = new ArrayList<>();

        if (emails != null && emails.length > 0 && emailCategories != null && emailCategories.length > 0)
            for (int i = 0; i < emails.length; i++)
                emailList.add(new Email(emails[i].trim(), emailCategories[i].trim()));

        return emailList;
    }

    /**
     * Convertir datos obtenidos del formulario en un array de tipo Phone[]
     *
     * @param countryCodes    Array de countryCodes
     * @param phones          Array de números de teléfono
     * @param phoneCategories Array de categorías de los teléfonos
     * @return Lista de tipo Phone
     */
    public static ArrayList<Phone> phonesFromArrays(String[] countryCodes, String[] phones, String[] phoneCategories) {
        ArrayList<Phone> phonesList = new ArrayList<>();

        if (countryCodes != null && countryCodes.length > 0 && phones != null && phones.length > 0)
            for (int i = 0; i < countryCodes.length; i++)
                phonesList.add(new Phone(countryCodes[i], phones[i], phoneCategories[i].trim()));


        return phonesList;
    }

    /**
     * Convertir los datos obtenidos del formulario en un array de tipo Address[]
     *
     * @param streets           Array de calles
     * @param houseNumbers      Array de números de casas
     * @param zips              Array de códigos postales
     * @param cities            Array de ciudades
     * @param addressCategories Array de categorías
     * @return Lista de tipo Address
     */
    public static ArrayList<Address> addressesFromArrays(String[] streets, String[] houseNumbers, String[] zips,
                                                    String[] cities, String[] addressCategories) {
        ArrayList<Address> addressesList = new ArrayList<>();

        if (
                streets != null           && streets.length > 0            &&
                houseNumbers != null      && houseNumbers.length > 0       &&
                zips != null              && zips.length > 0               &&
                cities != null            && cities.length > 0             &&
                addressCategories != null && addressCategories.length > 0
        )
            for (int i = 0; i < streets.length; i++)
                addressesList.add(
                        new Address(streets[i].trim(),
                                houseNumbers[i].trim(),
                                zips[i].trim(),
                                cities[i].trim(),
                                addressCategories[i].trim())
                );

        return addressesList;
    }
}
