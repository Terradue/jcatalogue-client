package com.terradue.jcatalogue;

import java.util.Map;

import com.terradue.jcatalogue.repository.LocalRepository;
import com.terradue.jcatalogue.repository.RemoteRepository;

public interface CatalogueSystemSession
{

    LocalRepository getLocalRepository();

    RemoteRepository getRemoteRepository();

    Map<String, String> getRequestParameters();

}
