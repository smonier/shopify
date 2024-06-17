import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useSiteInfo, useNodeInfo } from '@jahia/data-helper';
import { useSelector } from 'react-redux';
import { LoaderOverlay } from '../DesignSystem/LoaderOverlay';

export const RequestProductsUpdate = ({ path, render: Render, ...otherProps }) => {
    const { t } = useTranslation('shopify');
    const { language, site } = useSelector(state => ({ language: state.language, site: state.site }));
    const { siteInfo, loading: siteLoading } = useSiteInfo({ siteKey: site, displayLanguage: language });
    const { node, loading: nodeLoading } = useNodeInfo({ path: path, language: language }, { getDisplayName: true });
    const [data, setData] = useState(null);
    const [loadingQuery, setLoadingQuery] = useState(false);
    const [error, setError] = useState(null);

    const handleClick = async () => {
        setLoadingQuery(true);
        setError(null);
        try {
            const response = await fetch(`${contextJsParameters.contextPath}/cms/editframe/default/${language}${path}.requestShopifyProductsUpdate.do`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', Accept: 'application/json' }
            });

            if (!response.ok) {
                const errorMessage = `HTTP error! status: ${response.status}`;
                const errorBody = await response.text();
                console.error(errorMessage, errorBody);
                throw new Error(errorMessage);
            }

            let data;
            try {
                data = await response.json();
            } catch (error) {
                console.error('Error parsing JSON:', error);
                throw new Error('Failed to parse JSON response');
            }

            if (data.resultCode === 200) {
                const shops = data.shop;
                let messageContent = "Products Update Result\n\n\n";
                shops.forEach(shop => {
                    console.log(`  Shop: ${shop.name}, Product Updated: ${shop.productCount}`);
                    messageContent += `Shop: ${shop.name}, Product Updated: ${shop.productCount}\n\n`;
                });

                alert(messageContent);
            } else {
                alert(`Error: ${data.resultCode}`);
            }
        } catch (error) {
            console.error('Error updating Shopify products:', error);

            let errorMessage = 'An error occurred while updating Shopify products. Please check the console for more details.';
            if (error instanceof Error) {
                errorMessage = error.message;
            }

            setError(errorMessage);
        } finally {
            setLoadingQuery(false);
        }
    };

    useEffect(() => {
        if (data) {
            alert(data);
        }
        if (error) {
            alert(error);
        }
    }, [data, error]);

    if (loadingQuery || siteLoading || !siteInfo || nodeLoading || !node) {
        return <LoaderOverlay status={true} />;
    }

    return (
        <>
            <Render
                {...otherProps}
                buttonLabel={t('label.requestShopifyProductsUpdate', { displayName: node.displayName })}
                onClick={handleClick}
            />
            <LoaderOverlay status={loadingQuery} />
        </>
    );
};
