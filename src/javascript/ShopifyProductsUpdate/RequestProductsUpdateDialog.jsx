import React, { useState, useEffect } from 'react';
import { Dialog, DialogActions, DialogContent, DialogTitle } from '@material-ui/core';
import { Button, Typography } from '@jahia/moonstone';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { useSiteInfo, useNodeInfo } from '@jahia/data-helper';
import { useSelector } from 'react-redux';
import { LoaderOverlay } from '../DesignSystem/LoaderOverlay';
import styles from './RequestProductsUpdateDialog.scss';
import { useQuery } from '@apollo/client';
import { GetShops } from '../Utils/shops.gql-queries';

export const RequestProductsUpdateDialog = ({ path, isOpen, onCloseDialog }) => {
    const { t } = useTranslation('shopify');
    const { language, site } = useSelector(state => ({ language: state.language, site: state.site }));
    const { siteInfo, loading: siteLoading } = useSiteInfo({ siteKey: site, displayLanguage: language });
    const { node, loading: nodeLoading } = useNodeInfo({ path: path, language: language }, { getDisplayName: true });
    const [shops, setShops] = useState([]);
    const [loadingQuery, setLoadingQuery] = useState(false);
    const [error, setError] = useState(null);
    const [results, setResults] = useState(null);

    const { data, errors, loading } = useQuery(GetShops, {
        variables: {
            path: path,
            language: language
        }
    });

    useEffect(() => {
        if (data && data.jcr && data.jcr.result && data.jcr.result.shops && data.jcr.result.shops.refNodes) {
            const fetchedShops = data.jcr.result.shops.refNodes.map((node, index) => ({
                name: node.name
            }));
            setShops(fetchedShops);
        }
        if (errors) {
            setError(errors[0].message);
        }
    }, [data, errors]);


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
                setShops(results.shop || []); // Ensure results.shop is not null
                setResults(results);
            } else {
                setShops([]);
                setError(`Error: ${results.resultCode}`);
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

    if (siteLoading || !siteInfo || nodeLoading || !node || loading) {
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
                <div className={styles.loaderOverlayWrapper}>
                    <LoaderOverlay status={loadingQuery} />
                </div>

                {results && shops.length > 0 && (
                    <div className={styles.textResults}>
                        <Typography variant="h4" className={styles.updateResultsTitle}>Products Update Results</Typography>
                        <div className={styles.gridContainer}>
                            <div className={styles.gridHeader}>Shop</div>
                            <div className={styles.gridHeader}>Products Created</div>
                            <div className={styles.gridHeader}>Products Updated</div>
                            {results && shops.map((shop, index) => (
                                <React.Fragment key={index}>
                                    <div className={styles.gridItem}>{shop.name}</div>
                                    <div className={styles.gridItem}>{shop.productCreated}</div>
                                    <div className={styles.gridItem}>{shop.productUpdated}</div>
                                </React.Fragment>
                            ))}
                        </div>
                    </div>
                )}
                {!results && shops.length > 0 && (
                    <div className={styles.textResults}>
                        <Typography variant="h4" className={styles.updateResultsTitle}>Products Update</Typography>
                        <ul>
                            {shops.map((shop, index) => (
                                <li key={index}>
                                    Shop: {shop.name}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
                {error && <Typography color="error">{error}</Typography>}
            </DialogContent>
            <DialogActions>
                {!results && <Button
                    size="big"
                    color="danger"
                    label={t('shopify:label.btnCancel.title')}
                    onClick={handleCancel}
                />}
                {!results && <Button
                    size="big"
                    color="accent"
                    label={t('shopify:label.btnApply.title')}
                    onClick={handleClick}
                />}
                {results && <Button
                    size="big"
                    color="accent"
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
