package ru.spbstu.controllers;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthRequest implements Serializable {
    private String userName;
    private String password;
}