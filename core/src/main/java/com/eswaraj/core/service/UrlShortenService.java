package com.eswaraj.core.service;

import com.eswaraj.core.exceptions.ApplicationException;

public interface UrlShortenService {

    String getShortUrl(String longUrl) throws ApplicationException;

}
