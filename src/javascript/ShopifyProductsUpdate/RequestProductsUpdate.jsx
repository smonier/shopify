import React, { useContext } from 'react';
import PropTypes from 'prop-types';
import { RequestProductsUpdateDialog } from './RequestProductsUpdateDialog';
import { ComponentRendererContext } from '@jahia/ui-extender';
import { useSiteInfo, useNodeInfo } from '@jahia/data-helper';
import { useSelector } from 'react-redux';

const RequestProductsUpdateComponent = ({ render: Render, path, ...otherProps }) => {
    const { render: renderComponent, destroy } = useContext(ComponentRendererContext);
    const { language, site } = useSelector(state => ({ language: state.language, site: state.site }));
    const { siteInfo, loading: siteLoading } = useSiteInfo({ siteKey: site, displayLanguage: language });
    const { node, loading: nodeLoading } = useNodeInfo({ path: path, language: language }, { getDisplayName: true });

    if (!node) {
        return null; // Optionally, handle loading state or errors here.
    }

    return (
        <Render
            {...otherProps}
            onClick={() => {
                renderComponent('RequestProductsUpdateDialog', RequestProductsUpdateDialog, {
                    isOpen: true,
                    uuid: node.uuid,
                    path: path,
                    onCloseDialog: () => destroy('RequestProductsUpdateDialog')
                });
            }}
        />
    );
};

RequestProductsUpdateComponent.propTypes = {
    render: PropTypes.func.isRequired,
    path: PropTypes.string.isRequired,
};

export const RequestProductsUpdate = {
    component: RequestProductsUpdateComponent,
};
