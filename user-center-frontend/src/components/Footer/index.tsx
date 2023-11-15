import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import {NANKAI_LINK} from "@/constants";
const Footer: React.FC = () => {
  const defaultMessage = 'Tong出品';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'nankai',
          title: '南开大学',
          href: NANKAI_LINK,
          blankTarget: true,
        },
        {
          key: 'github',
          title: <><GithubOutlined /> Tong Github</>,
          href: 'https://github.com/CN-Tong',
          blankTarget: true,
        },
        {
          key: 'nankaiPhysics',
          title: '南开大学物理学院',
          href: 'https://physics.nankai.edu.cn/',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
