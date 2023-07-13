import React from 'react';

import { IconButton } from '../../../../shared/ui';

export const FourIconButton = (props) => {



    return(
        <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
          }}>
            <div>
              <IconButton textIcon={'Pozitive vote'}/>
            </div>
            <div>
              <IconButton textIcon={'Negative vote'}/>
            </div>
            <div>
              <IconButton textIcon={'Edit task'}/>
            </div>
            <div>
              <IconButton textIcon={'Garbage task'}/>
            </div>
          </div> 
    )
}
