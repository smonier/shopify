import * as React from 'react';

export interface FilterSelectProps {
    classes: Object;
    options: any[];
    value?: string;
    onChange?: (...args: any[])=>any;
}

export class FilterSelect extends React.Component<FilterSelectProps, any> {
    render(): JSX.Element;

}

