import * as React from 'react';

export interface PaginationProps {
    totalCount: number;
    pageSize: number;
    currentPage: number;
    onChangeRowsPerPage: (...args: any[])=>any;
    onChangePage: (...args: any[])=>any;
}

export class Pagination extends React.Component<PaginationProps, any> {
    render(): JSX.Element;

}

