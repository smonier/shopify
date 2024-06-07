import React from 'react';
import {useTranslation} from 'react-i18next';
import {useSiteInfo, useNodeInfo} from '@jahia/data-helper';
import {useSelector} from 'react-redux';

export const RequestProductsUpdate = ({path, render: Render, ...otherProps}) => {
    const {t} = useTranslation('shopify');
    const {language, site} = useSelector(state => ({language: state.language, site: state.site}));
    const {siteInfo, loading} = useSiteInfo({siteKey: site, displayLanguage: language});
    const {node, nodeLoading: nodeLoading} = useNodeInfo({path: path, language: language}, {getDisplayName: true});

    if (loading || !siteInfo || nodeLoading || !node) {
        return null;
    }

        return <Render {...otherProps}
            buttonLabel={t('label.requestShopifyProductsUpdate', {displayName: node.displayName})}
            onClick={async () => {
                            const response = await fetch(`${contextJsParameters.contextPath}/cms/editframe/default/${language}${path}.requestShopifyProductsUpdate.do`, {
                                method: 'POST'
                            });
                        }}/>

};
