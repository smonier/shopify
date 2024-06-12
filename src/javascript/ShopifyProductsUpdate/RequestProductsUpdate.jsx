import React from 'react';
import { useTranslation } from 'react-i18next';
import { useSiteInfo, useNodeInfo } from '@jahia/data-helper';
import { useSelector } from 'react-redux';

export const RequestProductsUpdate = ({ path, render: Render, ...otherProps }) => {
    const { t } = useTranslation('shopify');
    const { language, site } = useSelector(state => ({ language: state.language, site: state.site }));
    const { siteInfo, loading } = useSiteInfo({ siteKey: site, displayLanguage: language });
    const { node, nodeLoading } = useNodeInfo({ path: path, language: language }, { getDisplayName: true });

    const handleClick = async () => {
        try {
            const response = await fetch(`${contextJsParameters.contextPath}/cms/editframe/default/${language}${path}.requestShopifyProductsUpdate.do`, {
                method: 'POST'
            });

            if (!response.ok) {
                const errorMessage = `HTTP error! status: ${response.status}`;
                const errorBody = await response.text(); // Get the response body text
                console.error(errorMessage, errorBody); // Log status and response body
                throw new Error(errorMessage);
            }

            const text = await response.text(); // Read the response body as text
            let data;
            try {
                data = text ? JSON.parse(text) : {}; // Parse JSON only if the text is not empty
            } catch (error) {
                console.error('Error parsing JSON:', error);
                throw new Error('Failed to parse JSON response');
            }

            if (data.resultCode === 200) {
                alert(`Success! Product count: ${data.productCount}`);
            } else {
                alert(`Error: ${data.resultCode}`);
            }
        } catch (error) {
            console.error('Error updating Shopify products:', error);

            // Check if error is an instance of Error with a message
            let errorMessage = 'An error occurred while updating Shopify products. Please check the console for more details.';
            if (error instanceof Error) {
                errorMessage = error.message;
            }

            alert(errorMessage);
        }
    };


    if (loading || !siteInfo || nodeLoading || !node) {
        return null;
    }

    return (
        <Render
            {...otherProps}
            buttonLabel={t('label.requestShopifyProductsUpdate', { displayName: node.displayName })}
            onClick={handleClick}
        />
    );
};
