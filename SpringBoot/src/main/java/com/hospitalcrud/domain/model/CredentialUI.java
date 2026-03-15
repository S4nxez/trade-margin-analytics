package com.hospitalcrud.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialUI {
    private String username;
    private String password;
}
