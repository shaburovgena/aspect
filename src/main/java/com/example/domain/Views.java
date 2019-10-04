package com.example.domain;

/**
 * Класс для сокрытия некоторых полей от клиента
 */
public final class Views {
    //Показывает только id клиенту
    public interface Id {
    }
    //Показывает и id и name
    public interface IdName extends Id {
    }
    public interface IdNameRoles extends IdName {
    }

    //Показывает профиль (и id и name)
    public interface FullProfile extends IdName{

    }
public interface FullProfilePassw extends FullProfile{

    }
}
