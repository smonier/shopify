import gql from 'graphql-tag';
import {PredefinedFragments} from '@jahia/data-helper';

export const GetShops = gql`
    query getShops($path: String!, $language: String!) {
        jcr {
            result: nodeByPath(path: $path) {
                ...NodeCacheRequiredFields
                value: uuid
                label: displayName(language: $language)
                shops: property(name: "shopName") {
                    refNodes {
                      ...NodeCacheRequiredFields
                      name: displayName(language: $language)
                    }
                }
            }
        }
    }
    ${PredefinedFragments.nodeCacheRequiredFields.gql}`;