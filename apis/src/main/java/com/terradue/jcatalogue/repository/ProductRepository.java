package com.terradue.jcatalogue.repository;

/**
 * A repository hosting satellite products.
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ProductRepository
{

    /**
     * Gets the type of the repository, for example "default".
     *
     * @return The (case-sensitive) type of the repository, never {@code null}.
     */
    String getContentType();

    /**
     * Gets the identifier of this repository.
     *
     * @return The (case-sensitive) identifier, never {@code null}.
     */
    String getId();

}
