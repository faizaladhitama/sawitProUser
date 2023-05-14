package com.sawitPro.viewModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Registration {
    private String phoneNumber;
    private String name;
    private String password;
}
