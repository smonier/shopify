import * as React from 'react';

export interface PickerTreeViewMaterialProps {
    classes: Object;
    dataCmRole: string;
    loading: boolean;
    onOpenItem?: (...args: any[])=>any;
    onSelectItem?: (...args: any[])=>any;
    pickerEntries: any[];
    rootLabel: string;
}

export class PickerTreeViewMaterial extends React.Component<PickerTreeViewMaterialProps, any> {
    render(): JSX.Element;
}