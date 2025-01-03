package com.korit.silverbutton.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklist = new HashSet<>();

    // 블랙리스트에 토큰을 추가하는 메소드
    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    // 토큰이 블랙리스트에 있는지 확인하는 메소드
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
