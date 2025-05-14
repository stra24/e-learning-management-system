package com.everrefine.elms.presentation.request;

import com.everrefine.elms.domain.model.user.UserRole;
import lombok.Data;

@Data
public class NewsUpdateRequest {

    private String id;
    private String title;
    private String content;
}
