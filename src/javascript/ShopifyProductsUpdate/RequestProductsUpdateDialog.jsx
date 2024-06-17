import React, { useState } from 'react';
import { Dialog, DialogActions, DialogContent, DialogTitle } from '@material-ui/core';
import { Button, Typography } from '@jahia/moonstone';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { useSiteInfo, useNodeInfo } from '@jahia/data-helper';
import { useSelector } from 'react-redux';
import { LoaderOverlay } from '../DesignSystem/LoaderOverlay';
import styles from './RequestProductsUpdateDialog.scss';

export const RequestProductsUpdateDialog = ({ path, isOpen, onCloseDialog}) => {
    const { t } = useTranslation('shopify');
    const { language, site } = useSelector(state => ({ language: state.language, site: state.site }));
    const { siteInfo, loading: siteLoading } = useSiteInfo({ siteKey: site, displayLanguage: language });
    const { node, loading: nodeLoading } = useNodeInfo({ path: path, language: language }, { getDisplayName: true });
    const [data, setData] = useState(null);
    const [loadingQuery, setLoadingQuery] = useState(false);
    const [error, setError] = useState(null);

    const handleCancel = () => {
        onCloseDialog();
    };

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

            let results;
            try {
                results = await response.json();
            } catch (error) {
                console.error('Error parsing JSON:', error);
                throw new Error('Failed to parse JSON response');
            }

            if (results.resultCode === 200) {
                const shops = results.shop;
                let messageContent = "Products Update Results";
                shops.forEach(shop => {
                    console.log(`  Shop: ${shop.name}, Product Updated: ${shop.productCount}`);
                    messageContent += `Shop: ${shop.name}, Product Updated: ${shop.productCount}`;
                });

                setData(messageContent);
            } else {
                setData(`Error: ${results.resultCode}`);
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

    if (siteLoading || !siteInfo || nodeLoading || !node) {
        return <LoaderOverlay status={true} />;
    }

    return (
        <Dialog
            fullWidth
            aria-labelledby="alert-dialog-slide-title"
            open={isOpen}
            maxWidth="sm"
            classes={{ paper: styles.dialog_overflowYVisible }}
            onClose={onCloseDialog}
        >
            <DialogTitle id="dialog-language-title">
                <Typography isUpperCase variant="heading" weight="bold" className={styles.dialogTitle}>
                    {t('shopify:label.requestShopifyProductsUpdate')}
                </Typography>
            </DialogTitle>
            <DialogContent className={styles.dialogContent} classes={{ root: styles.dialogContent_overflowYVisible }}>
                <LoaderOverlay status={loadingQuery} />
                {data && <Typography>{data}</Typography>}
                {error && <Typography color="error">{error}</Typography>}
            </DialogContent>
            <DialogActions>
                {!loadingQuery && <Button
                    size="big"
                    color="default"
                    label={t('shopify:label.btnCancel.title')}
                    onClick={handleCancel}
                />}
                {!loadingQuery && <Button
                    size="big"
                    color="accent"
                    label={t('shopify:label.btnApply.title')}
                    onClick={handleClick}
                />}
                {loadingQuery && <Button
                    size="big"
                    color="default"
                    label={t('shopify:label.btnClose.title')}
                    onClick={handleCancel}
                />}
            </DialogActions>
        </Dialog>
    );
};

RequestProductsUpdateDialog.propTypes = {
    path: PropTypes.string.isRequired,
    isOpen: PropTypes.bool.isRequired,
    onCloseDialog: PropTypes.func.isRequired
};
