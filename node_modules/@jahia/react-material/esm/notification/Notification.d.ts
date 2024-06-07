import * as React from 'react';

export interface NotificationProps {
    message?: string;
    predefinedOptions?: string[];
    options?: Object;
}

export class Notification extends React.Component<NotificationProps, any> {
    render(): JSX.Element;

}

