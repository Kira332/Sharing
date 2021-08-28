package com.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenUtils {
    public String username;
    public Serializable tokenId;
}
