package com.cos.photogramstart.web.dto.subscribe;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeDTO {
private int id;
private String username;
private String profileImageUrl;
private Integer subscribeState;
private Integer equalUserState;
}
