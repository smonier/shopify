import * as React from 'react';

export interface PickerViewMaterialMultipleProps {
    pickerEntries: any[];
    onSelectItem?: (...args: any[])=>any;
    onOpenItem?: (...args: any[])=>any;
    textRenderer?: (...args: any[])=>any;
}

export const PickerViewMaterialMultiple: React.SFC<PickerViewMaterialMultipleProps>;

