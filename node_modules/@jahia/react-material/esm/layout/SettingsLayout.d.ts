import * as React from 'react';

export interface SettingsLayoutProps {
    appBar?: React.ReactElement<any>;
    footer?: string;
}

export class SettingsLayout extends React.Component<SettingsLayoutProps, any> {
    render(): JSX.Element;

}

