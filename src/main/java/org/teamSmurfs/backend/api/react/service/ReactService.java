package org.teamSmurfs.backend.api.react.service;

import org.teamSmurfs.backend.api.react.dto.CreateReactRequest;
import org.teamSmurfs.backend.api.react.dto.DeleteReactRequest;

public interface ReactService {
    void createReact(final CreateReactRequest createReactRequest);
    void deleteReact(final DeleteReactRequest deleteReactRequest);
}
