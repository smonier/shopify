import {registry} from '@jahia/ui-extender';
import register from './ShopifyProductsUpdate/Shopify.register';

export default function () {
    registry.add('callback', 'shopifyProductsUpdate', {
        targets: ['jahiaApp-init:50'],
        callback: register
    });
}

console.debug('%c Shopify Products Update is activated', 'color: #3c8cba');