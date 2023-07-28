import React from 'react';

import { IconButton } from '../../../../shared/ui';
import pozitive from './img/positive-vote.png'
import negative from './img/negative-vote.png'
import edit from './img/edit.png'
import garbage from './img/garbage.png'

export const FourIconButton = (props) => {



    return(
        <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
          }}>
            <div>
              <IconButton textIcon={'Pozitive vote'} img={pozitive} />
            </div>
            <div>
              <IconButton textIcon={'Negative vote'} img={negative} />
            </div>
            <div>
              <IconButton textIcon={'Edit task'} img={edit} />
            </div>
            <div>
              <IconButton textIcon={'Garbage task'} img={garbage} />
            </div>
          </div> 
    )
}
