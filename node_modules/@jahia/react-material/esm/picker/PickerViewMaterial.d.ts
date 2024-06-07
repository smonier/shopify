import * as React from 'react';

export interface PickerViewMaterialProps {
    pickerEntries: any[];
    onSelectItem?: (...args: any[])=>any;
    onOpenItem?: (...args: any[])=>any;
    textRenderer?: (...args: any[])=>any;
}

export const PickerViewMaterial: React.SFC<PickerViewMaterialProps>;

