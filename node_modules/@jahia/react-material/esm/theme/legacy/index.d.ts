import {createMuiTheme} from '@material-ui/core';
import * as _ from 'lodash';

import {legacyTypography} from './overrides/typography';
import {legacyButton, legacyIconButton} from './overrides/button';
import {legacyCheckbox} from './overrides/checkbox';
import {legacyCollapse} from './overrides/collapse';
import {legacyDialogActions, legacyDialogContent, legacyDialogContentText, legacyDialogTitle} from './overrides/dialog';
import {legacyFormControl, legacyFormControlLabel, legacyFormHelperText} from './overrides/form';
import {legacyInput} from './overrides/input';
import {legacyListItemText} from './overrides/list';
import {legacyMenuItem} from './overrides/menu';
import {legacyPaper} from './overrides/paper';
import {legacySelect} from './overrides/select';
import {legacySwitch} from './overrides/switch';
import {legacyTableCell, legacyTableRow} from './overrides/table';
import {commonBaseThemeConfig} from '../common-base/config';

const legacyThemeConfig = {
    overrides: {
        MuiButton: legacyButton,
        MuiIconButton: legacyIconButton,
        MuiCheckbox: legacyCheckbox,
        MuiCollapse: legacyCollapse,
        MuiDialogTitle: legacyDialogTitle,
        MuiDialogContent: legacyDialogContent,
        MuiDialogContentText: legacyDialogContentText,
        MuiDialogActions: legacyDialogActions,
        MuiFormControl: legacyFormControl,
        MuiFormHelperText: legacyFormHelperText,
        MuiFormControlLabel: legacyFormControlLabel,
        MuiInput: legacyInput,
        MuiListItemText: legacyListItemText,
        MuiMenuItem: legacyMenuItem,
        MuiPaper: legacyPaper,
        MuiSelect: legacySelect,
        MuiSwitch: legacySwitch,
        MuiTableRow: legacyTableRow,
        MuiTableCell: legacyTableCell,
        MuiTypography: legacyTypography
    }
};

const legacyTheme = createMuiTheme(_.merge({}, commonBaseThemeConfig, legacyThemeConfig));

export {legacyTheme};
