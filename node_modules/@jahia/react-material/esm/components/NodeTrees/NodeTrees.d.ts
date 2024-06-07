import * as React from 'react';

export interface NodeTreesProps {
    path: string;
    siteKey: string;
    classes: Object;
    nodeTreeConfigs: any[];
    setRefetch?: (...args: any[]) => any;
    children: (...args: any[]) => any;
}

export class NodeTrees extends React.Component<NodeTreesProps, any> {
    render(): JSX.Element;
}
